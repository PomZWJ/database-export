package com.pomzwj.dbservice.clickhouse;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pomzwj.dbpool.DbPoolFactory;
import com.pomzwj.dbservice.AbstractDbService;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class ClickhouseDbService extends AbstractDbService implements DbService {
    static final Logger log = LoggerFactory.getLogger(ClickhouseDbService.class);
    @Autowired
    private DbPoolFactory dbPoolFactory;
    @Value("${database.getTableNameSql.clickhouse}")
    String clickhouseGetTableNameSql;
    @Override
    public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
        //获取数据库线程池
        DataSource dbPool = null;
        try {
            dbPool = dbPoolFactory.getDbPoolServiceBean().createDbPool(dbBaseInfo);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbPool);
            //1.获取所有表基本信息
            List<DbTable> tableName = this.getTableName(jdbcTemplate, dbBaseInfo.getDbName());
            //2.1 把List进行分页
            List[] listArray = this.listToPageArray(tableName);
            //2.2进行表列的数据获取
            this.getTableColumnInfoByMultiThread(jdbcTemplate, listArray,dbBaseInfo.getDbName());
            return tableName;
        } catch (Exception e) {
            log.error("获取表信息失败e={}",e);
            throw e;
        } finally {
            //关闭线程池
            dbPoolFactory.getDbPoolServiceBean().closeDbPool(dbPool);
        }
    }
    private List<DbTable> getTableName(JdbcTemplate jdbcTemplate, String dbName) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(String.format(clickhouseGetTableNameSql, dbName));
        List<DbTable> tableList = this.getTableNameAndComments(resultList);
        return tableList;
    }
    private void getTableColumnInfoByMultiThread(JdbcTemplate jdbcTemplate, List[] listArray,String dbName) throws Exception {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        ExecutorService es = new ThreadPoolExecutor(5,10,200L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),namedThreadFactory);
        try{
            List<Future> resultFuture = new ArrayList<>();
            for (int i = 0; i < listArray.length; i++) {
                int finalI = i;
                Future<Boolean> submit = es.submit(() -> {
                    try {
                        List<DbTable> list = (List<DbTable>) listArray[finalI];
                        setColumnDataInfo(jdbcTemplate,list,dbName);
                        return true;
                    } catch (Exception e) {
                        log.error("多线程查询表的列信息失败,e={}", e);
                    }
                    return false;
                });
                resultFuture.add(submit);
            }
            for (int i = 0; i < resultFuture.size(); i++) {
                Boolean o = (Boolean) resultFuture.get(i).get();
                if (!o) {
                    throw new DatabaseExportException("导出word失败");
                }
            }
        }finally {
            if(es != null){
                es.shutdown();
            }
        }
    }
    public void setColumnDataInfo(JdbcTemplate jdbcTemplate,List<DbTable> list,String dbName){
        for (int j = 0; j < list.size(); j++) {
            DbTable dbTable = list.get(j);
            //因为有的没有comment,这边是做个错误判断
            try{
                Map<String, Object> engineAndCommentMap = jdbcTemplate.queryForMap(String.format("select comment, engine from system.tables WHERE database = '%s' and name = '%s'", dbName, dbTable.getTableName()));
                String comment = MapUtils.getString(engineAndCommentMap, "comment", "");
                String engine = MapUtils.getString(engineAndCommentMap, "engine", "");
                if(StringUtils.isEmpty(comment) && StringUtils.isEmpty(engine)){
                    dbTable.setTableComments(comment+" engine:"+engine);
                }else if(StringUtils.isNotEmpty(comment) && StringUtils.isEmpty(engine)){
                    dbTable.setTableComments(comment);
                }else if(StringUtils.isEmpty(comment) && StringUtils.isNotEmpty(engine)){
                    dbTable.setTableComments("engine:"+engine);
                }else{
                    dbTable.setTableComments("");
                }
            }catch (Exception e){
                Map<String, Object> engineAndCommentMap = jdbcTemplate.queryForMap(String.format("select '' as comment,engine from system.tables WHERE database = '%s' and name = '%s'", dbName, dbTable.getTableName()));
                String engine = MapUtils.getString(engineAndCommentMap, "engine", "");
                if(StringUtils.isNotEmpty(engine)){
                    dbTable.setTableComments("engine:"+engine);
                }else{
                    dbTable.setTableComments("");
                }
            }
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList("desc "+dbName+"."+dbTable.getTableName());
            List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> resultSet : resultList) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(MapUtils.getString(resultSet, "name"));
                    dbColumnInfo.setDataType(this.getDataType(MapUtils.getString(resultSet, "type")));
                    dbColumnInfo.setNullAble(this.isNullable(MapUtils.getString(resultSet, "type")));
                    dbColumnInfo.setDefaultVal(
                            this.getDefaultValue(MapUtils.getString(resultSet, "default_type",""),
                                    MapUtils.getString(resultSet, "default_expression","")));
                    //dbColumnInfo.setAutoIncrement(false);
                    //dbColumnInfo.setPrimary(false);
                    String comments = MapUtils.getString(resultSet, "comment");
                    if (StringUtils.isEmpty(comments)) {
                        dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                    } else {
                        dbColumnInfo.setComments(comments);
                    }
                    String extraInfo = MapUtils.getString(resultSet, "EXTRA_INFO");
                    if (!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")) {
                        dbColumnInfo.setAutoIncrement(true);
                    }
                    String columnKey = MapUtils.getString(resultSet, "COLUMN_KEY");
                    if (!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")) {
                        dbColumnInfo.setPrimary(true);
                    }
                    dbColumnInfos.add(dbColumnInfo);
                }
                dbTable.setTabsColumn(dbColumnInfos);
            }
        }
    }
    private boolean isNullable(String var){
        if(StringUtils.isNotEmpty(var)){
            if(var.startsWith("Nullable")){
                return true;
            }
        }
        return false;
    }
    private String getDataType(String var){
        if(StringUtils.isNotEmpty(var)){
            if(var.startsWith("Nullable")){
                return var.substring(9,var.length()-1);
            }else{
                return var;
            }
        }
        return "";
    }
    private String getDefaultValue(String defaultType,String defaultExpression){
        if("DEFAULT".equalsIgnoreCase(defaultType)){
            return defaultExpression;
        }else{
            return "";
        }
    }
}
