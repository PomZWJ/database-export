package io.github.pomzwj.dbexport.core.dbservice;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.pomzwj.dbexport.core.domain.*;
import io.github.pomzwj.dbexport.core.exception.DatabaseExportException;
import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.utils.JdbcUrlParseUtils;
import io.github.pomzwj.dbexport.core.utils.JdbcUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * dbService中间类
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public abstract class AbstractDbService implements DbService {
    static final Logger log = LoggerFactory.getLogger(AbstractDbService.class);

    /**
     * 获取表详情
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Override
    public List<DbTable> getTableDetailInfo(DataSource dataSource, DbExportConfig dbExportConfig){
        this.initThreadLocalData(dataSource, dbExportConfig);
        //1.获取所有表基本信息
        List<DbTable> tableName = this.getTableName();
        //2.1 把List进行分页
        List[] listArray = this.listToPageArray(tableName);
        //2.2进行表列的数据获取
        this.getTableColumnInfoByMultiThread(listArray);
        return tableName;
    }

    @Override
    public List<DbTable> getTableList(DataSource dataSource, DbExportConfig dbExportConfig){
        this.initThreadLocalData(dataSource,dbExportConfig);
        try {
            //1.获取所有表基本信息
            return this.getTableName();
        } catch (Exception e) {
            log.error("getTableList error", e);
            throw e;
        }
    }

    private void initThreadLocalData(DataSource dataSource, DbExportConfig dbExportConfig){
        dbBaseInfoThreadLocal.set(JdbcUrlParseUtils.parseConnectionUrlToBaseInfo(dataSource));
        dataSourceThreadLocal.set(dataSource);
        dbExportConfigThreadLocal.set(dbExportConfig);
    }

    public List<Map<String, Object>> queryListMapDataBase(DataSource dataSource,String sql,Object...params){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
            List<Map<String, Object>> resultList = runner.query(connection,sql,new MapListHandler(),params);
            return resultList;
        }catch (SQLException e){
            log.error("queryListMapDataBase fail",e);
        }finally {
            JdbcUtils.close(connection);
        }
        return new ArrayList<>();
    }


    /**
     * 把List进行分页
     *
     * @param dbTableList
     * @return
     */
    private List[] listToPageArray(List<DbTable> dbTableList) {
        if (CollectionUtils.isEmpty(dbTableList)) {
            throw new DatabaseExportException(MessageCode.DATABASE_TABLE_NOT_ANY);
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
     *
     * @param resultList
     * @return
     */
    public List<DbTable> getTableNameAndComments(List<Map<String, Object>> resultList) {
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
                    dbTable.setTableComments(StringUtils.trimLineBreak(tableComments));
                }
                dbTable.setTableName(tableName);
                tableList.add(dbTable);
            }
        }
        return tableList;
    }

    /**
     * 获取详细的表信息
     * @param listArray
     * @throws Exception
     */
    private void getTableColumnInfoByMultiThread(List[] listArray) {
        DbExportConfig dbExportConfig = dbExportConfigThreadLocal.get();
        DataSource dataSource = dataSourceThreadLocal.get();
        DbBaseInfo dbBaseInfo = dbBaseInfoThreadLocal.get();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        ExecutorService es = new ThreadPoolExecutor(5, 10, 200L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
        try {
            List<Future> resultFuture = new ArrayList<>();
            for (int i = 0; i < listArray.length; i++) {
                int finalI = i;
                Future<Boolean> submit = es.submit(() -> {
                    try {
                        List<DbTable> list = (List<DbTable>) listArray[finalI];
                        for (int j = 0; j < list.size(); j++) {
                            DbTable dbTable = list.get(j);

                            List<Map<String, Object>> resultList = this.queryListMapDataBase(dataSource,this.getQueryTableDetailSql(dbBaseInfo,dbTable.getTableName()));
                            dbTable.setTabsColumn(this.setColumnDataInfo(resultList,dbExportConfig.getDbColumnInfoDynamicClazz()));
                            if(dbExportConfig.isSearchIndex()){
                                List<Map<String, Object>> indexResultList = this.queryListMapDataBase(dataSource, this.getIndexSql(dbBaseInfo, dbTable.getTableName()));
                                dbTable.setTabsIndex(this.setIndexDataInfo(indexResultList,dbExportConfig.getDbIndexInfoDynamicClazz()));
                            }
                        }
                        return true;
                    } catch (Exception e) {
                        log.error("getTableColumnInfoByMultiThread failed", e);
                    }
                    return false;
                });
                resultFuture.add(submit);
            }
            for (int i = 0; i < resultFuture.size(); i++) {
                boolean o = false;
                try {
                    o = (Boolean) resultFuture.get(i).get();
                } catch (InterruptedException|ExecutionException e) {
                    throw new RuntimeException(e);
                }
                if (!o) {
                    throw new DatabaseExportException("getTableColumnInfoByMultiThread fail");
                }
            }
        } finally {
            if (es != null) {
                es.shutdown();
            }
        }
    }

    /**
     * 获取所有的表名称
     * @return
     */
    public List<DbTable> getTableName() {
        DbExportConfig dbExportConfig = dbExportConfigThreadLocal.get();
        List<DbTable> tableList = this.getQueryTableInfoSql();
        if (CollectionUtils.isEmpty(dbExportConfig.getSelectTableList())) {
            return tableList;
        }
        List<DbTable> tableListTarget = new ArrayList<>(tableList.size());
        for (DbTable item : tableList) {
            if (dbExportConfig.getSelectTableList().contains(item.getTableName())) {
                tableListTarget.add(item);
            }
        }
        return tableListTarget;
    }


    /**
     * 查询表详细信息的sql位置
     *
     * @return
     */
    public abstract String getQueryTableDetailSql(DbBaseInfo dbBaseInfo,String tableName);

    /**
     * 获取查询表信息的sql
     *
     * @return
     */
    public abstract List<DbTable> getQueryTableInfoSql();

    /**
     * 获取索引SQL
     * @return
     */
    public abstract String getIndexSql(DbBaseInfo dbBaseInfo,String tableName);

    /**
     * 解析表字段
     * @param resultList
     * @param dbColumBean
     * @return
     */
    public abstract List<DbColumnInfo> setColumnDataInfo(List<Map<String, Object>> resultList,Class<? extends DbColumnInfo> dbColumBean);

    /**
     * 解析表索引
     * @param resultList
     * @param dbIndexBean
     * @return
     */
    public abstract List<DbIndexInfo> setIndexDataInfo(List<Map<String, Object>> resultList,Class<? extends DbIndexInfo> dbIndexBean);
}
