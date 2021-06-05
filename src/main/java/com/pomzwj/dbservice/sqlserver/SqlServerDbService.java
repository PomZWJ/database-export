package com.pomzwj.dbservice.sqlserver;

import com.pomzwj.dbservice.DbService;
import com.pomzwj.domain.*;
import com.pomzwj.utils.DbConnnecttion;
import com.pomzwj.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class SqlServerDbService implements DbService {

    static final Logger log = LoggerFactory.getLogger(SqlServerDbService.class);

    @Value("${database.jdbc.sqlServer}")
    String sqlServerJdbc;
    @Value("${database.driver.sqlServer}")
    String sqlServerDriver;
    @Value("${database.getTableNameSql.sqlServer}")
    String sqlServerGetTableNameSql;

    @Override
    public List<String> initRowName() {
        return null;
    }

    @Override
    public List<TempData> getWordTempData(List<DbTable> tableMessage) {
        return null;
    }

    @Override
    public List<DbTable> getTableName(DbBaseInfo dbBaseInfo) throws Exception {
        List<DbTable> tableList = new ArrayList<>();

        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();

        ResultSet resultSet = null;
        try {
            String.format(sqlServerJdbc,ip,port,dbName);
            Connection connection = DbConnnecttion.getConn(sqlServerJdbc, userName, password, sqlServerDriver);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlServerGetTableNameSql);
            while (resultSet.next()) {
                DbTable dbTable = new DbTable();
                String tableName = resultSet.getString("TABLE_NAME");
                String tableComments = resultSet.getString("COMMENTS");
                if(StringUtils.isEmpty(tableComments)){
                    dbTable.setTableComments(FiledDefaultValue.TABLE_COMMENTS_DEFAULT);
                }else{
                    dbTable.setTableComments("("+tableComments+")");
                }
                dbTable.setTableName(tableName);
                tableList.add(dbTable);
            }
        } catch (Exception e) {
            log.error("获取所有表数据发生错误 = {}",e);
            throw e;
        } finally {
            DbConnnecttion.closeRs(resultSet);
        }
        return tableList;
    }

    @Override
    public void getTabsColumnInfo(DbBaseInfo dbBaseInfo,List<DbTable> dbTableList) throws Exception {
        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();

        List<DbColumnInfo> list = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            String.format(sqlServerJdbc,ip,port,dbName);
            Connection connection = DbConnnecttion.getConn(sqlServerJdbc, userName, password, sqlServerDriver);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlServerGetTableNameSql);
            while (resultSet.next()) {
                DbColumnInfo dbColumnInfo = new DbColumnInfo();
                dbColumnInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
                dbColumnInfo.setDataType(resultSet.getString("COLUMN_TYPE"));
                dbColumnInfo.setDataLength(resultSet.getString("DATA_LENGTH"));
                dbColumnInfo.setNullAble(resultSet.getString("NULLABLE"));
                dbColumnInfo.setDefaultVal(resultSet.getString("DATA_DEFAULT"));
                dbColumnInfo.setAutoIncrement(false);
                dbColumnInfo.setPrimary(false);
                String comments = resultSet.getString("COMMENTS");
                if (StringUtils.isEmpty(comments)) {
                    dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                } else {
                    dbColumnInfo.setComments(comments);
                }
                String extraInfo = resultSet.getString("EXTRA_INFO");
                if(!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")){
                    dbColumnInfo.setAutoIncrement(true);
                }
                String columnKey = resultSet.getString("COLUMN_KEY");
                if(!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")){
                    dbColumnInfo.setPrimary(true);
                }
                list.add(dbColumnInfo);
            }
        }catch (Exception e){
            log.error("获取表结构数据发生错误 = {}",e);
            throw e;
        } finally {
            DbConnnecttion.closeRs(resultSet);
        }
    }
}
