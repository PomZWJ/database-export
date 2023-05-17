package com.pomzwj.dbservice;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pomzwj.constant.DataBaseType;
import com.pomzwj.dbpool.DbPoolFactory;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.utils.SpringBeanContext;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * dbService中间类
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public abstract class AbstractDbService implements DbService {
    static final Logger log = LoggerFactory.getLogger(AbstractDbService.class);
    protected DbPoolFactory dbPoolFactory;

    /**
     * 获取表详情
     * @param dbBaseInfo
     * @return
     * @throws Exception
     */
    @Override
    public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
        //获取数据库线程池
        dbPoolFactory = SpringBeanContext.getContext().getBean(DbPoolFactory.class);
        DataSource dbPool = null;
        try {
            dbPool = dbPoolFactory.getDbPoolServiceBean().createDbPool(dbBaseInfo);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbPool);
            //1.获取所有表基本信息
            List<DbTable> tableName = this.getTableName(jdbcTemplate, dbBaseInfo);
            //2.1 把List进行分页
            List[] listArray = this.listToPageArray(tableName);
            //2.2进行表列的数据获取
            this.getTableColumnInfoByMultiThread(jdbcTemplate, listArray,dbBaseInfo);
            return tableName;
        } catch (Exception e) {
            log.error("获取表信息失败e={}",e);
            throw e;
        } finally {
            //关闭线程池
            dbPoolFactory.getDbPoolServiceBean().closeDbPool(dbPool);
        }
    }

    /**
     * 把List进行分页
     * @param dbTableList
     * @return
     */
    private List[] listToPageArray(List<DbTable> dbTableList){
        if(CollectionUtils.isEmpty(dbTableList)){
            throw new DatabaseExportException("该数据库中没有找到任何表");
        }
        int totalNum = dbTableList.size();
        List[] listArray = null;
        //如果表数量大于100则启用多线程
        if (totalNum < 100) {
            listArray = new ArrayList[1];
            listArray[0] = dbTableList;
        } else {
            int pageSize = 10;
            int totalPage = (totalNum + pageSize - 1) / pageSize;
            boolean finishFlag = false;
            listArray = new ArrayList[totalPage];
            for (int i = 0; i < totalPage; i++) {
                List<DbTable> temp = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    int index = i * pageSize + j;
                    if (index >= totalNum) {
                        finishFlag = true;
                        break;
                    }
                    temp.add(dbTableList.get(index));
                }
                listArray[i] = temp;
                if (finishFlag) {
                    break;
                }
            }
        }
        return listArray;
    }

    /**
     * 获取表名和注释
     * @param resultList
     * @return
     */
    public List<DbTable> getTableNameAndComments(List<Map<String, Object>> resultList){
        List<DbTable> tableList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> resultMap = resultList.get(i);
                DbTable dbTable = new DbTable();
                String tableName = MapUtils.getString(resultMap, "TABLE_NAME");
                String tableComments = MapUtils.getString(resultMap, "COMMENTS");
                if (StringUtils.isEmpty(tableComments)) {
                    dbTable.setTableComments(FiledDefaultValue.TABLE_COMMENTS_DEFAULT);
                } else {
                    dbTable.setTableComments(tableComments);
                }
                dbTable.setTableName(tableName);
                tableList.add(dbTable);
            }
        }
        return tableList;
    }

    /**
     * 获取详细的表信息
     * @param jdbcTemplate
     * @param listArray
     * @throws Exception
     */
    private void getTableColumnInfoByMultiThread(JdbcTemplate jdbcTemplate, List[] listArray,DbBaseInfo dbBaseInfo) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(this.getQueryTableDetailSql());
        InputStream inputStream = classPathResource.getInputStream();
        if (inputStream == null) {
            throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        ExecutorService es = new ThreadPoolExecutor(5,10,200L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),namedThreadFactory);
        try{
            String executeSql = IOUtils.toString(inputStream, DbService.DefaultCharsetName);
            List<Future> resultFuture = new ArrayList<>();
            for (int i = 0; i < listArray.length; i++) {
                int finalI = i;
                Future<Boolean> submit = es.submit(() -> {
                    try {
                        List<DbTable> list = (List<DbTable>) listArray[finalI];
                        this.setColumnDataInfo(jdbcTemplate,list,executeSql,dbBaseInfo);
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
                    throw new DatabaseExportException("导出失败");
                }
            }
        }finally {
            if(es != null){
                es.shutdown();
            }
            if(inputStream!=null){
                inputStream.close();
            }

        }
    }

    /**
     * 查询表详细信息的sql位置
     * @return
     */
    public abstract String getQueryTableDetailSql();

    /**
     * 获取查询表信息的sql
     * @return
     */
    public abstract String getQueryTableInfoSql();

    /**
     * 解析表字段
     * @param jdbcTemplate
     * @param list
     * @param executeSql
     * @param dbBaseInfo
     */
    public abstract void setColumnDataInfo(JdbcTemplate jdbcTemplate, List<DbTable> list, String executeSql, DbBaseInfo dbBaseInfo);

    /**
     * 获取所有的表名称
     * @param jdbcTemplate
     * @param dbBaseInfo
     * @return
     */
    public List<DbTable> getTableName(JdbcTemplate jdbcTemplate, DbBaseInfo dbBaseInfo){
        DataBaseType dbKindEnum = dbBaseInfo.getDbKindEnum();
        if(dbKindEnum.equals(DataBaseType.MYSQL)
                || dbKindEnum.equals(DataBaseType.CLICKHOUSE)){
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(String.format(getQueryTableInfoSql(), dbBaseInfo.getDbName()));
            List<DbTable> tableList = this.getTableNameAndComments(resultList);
            return tableList;
        }else if(dbKindEnum.equals(DataBaseType.ORACLE)
            || dbKindEnum.equals(DataBaseType.POSTGRESQL)
            || dbKindEnum.equals(DataBaseType.SQLITE)
            || dbKindEnum.equals(DataBaseType.SQLSERVER)){
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(getQueryTableInfoSql());
            List<DbTable> tableList = this.getTableNameAndComments(resultList);
            return tableList;
        }else{
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(getQueryTableInfoSql());
            List<DbTable> tableList = this.getTableNameAndComments(resultList);
            return tableList;
        }
    }
}
