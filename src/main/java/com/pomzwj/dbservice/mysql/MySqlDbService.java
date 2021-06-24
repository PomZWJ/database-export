package com.pomzwj.dbservice.mysql;

import com.pomzwj.dbservice.DbService;
import com.pomzwj.domain.*;
import com.pomzwj.utils.DbConnnecttion;
import com.pomzwj.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql数据库操作
 */
@Component
public class MySqlDbService implements DbService {

    static final Logger log = LoggerFactory.getLogger(MySqlDbService.class);

    @Value("${database.jdbc.mysql}")
    String mysqlJdbc;
    @Value("${database.driver.mysql}")
    String mysqlDriver;
    @Value("${database.getTableNameSql.mysql}")
    String mysqlGetTableNameSql;

    @Override
    public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();
        String jdbcStr = String.format(mysqlJdbc,ip,port,dbName);
        Connection connection = null;
        try {
            connection = DbConnnecttion.getConn(jdbcStr, userName, password, mysqlDriver);
            //1.获取所有表基本信息
            List<DbTable> tableName = this.getTableName(connection, dbName);
            //2.获取每张表的每一列信息
            this.getTabsColumnInfo(connection,tableName);
            return tableName;
        }catch (Exception e){
            log.error("发生错误 = {}",e);
            throw e;
        }finally {
            DbConnnecttion.closeConn(connection);
        }
    }


    private List<DbTable> getTableName(Connection connection, String dbName) throws Exception {
        List<DbTable> tableList = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(mysqlGetTableNameSql, dbName));
            while (resultSet.next()) {
                DbTable dbTable = new DbTable();
                String tableName = resultSet.getString("TABLE_NAME");
                String tableComments = resultSet.getString("COMMENTS");
                if (StringUtils.isEmpty(tableComments)) {
                    dbTable.setTableComments(FiledDefaultValue.TABLE_COMMENTS_DEFAULT);
                } else {
                    dbTable.setTableComments(tableComments);
                }
                dbTable.setTableName(tableName);
                tableList.add(dbTable);
            }
        }catch (Exception e){
            log.error("获取所有表发生错误 = {}",e);
            throw e;
        }finally {
            DbConnnecttion.closeResultSet(resultSet);
            DbConnnecttion.closeStat(statement);
        }
        return tableList;
    }

    private void getTabsColumnInfo(Connection connection, List<DbTable> dbTableList) throws Exception {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        ClassPathResource classPathResource = new ClassPathResource("sql/mysql.sql");
        InputStream inputStream = classPathResource.getInputStream();
        if (inputStream == null) {
            throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
        }
        try{
            String executeSql = IOUtils.toString(inputStream, DbService.DefaultCharsetName);
            preparedStatement = connection.prepareStatement(executeSql);
            for (int i = 0; i < dbTableList.size(); i++) {
                List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
                DbTable dbTable = dbTableList.get(i);
                preparedStatement.setString(1, dbTable.getTableName());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
                    dbColumnInfo.setDataType(resultSet.getString("COLUMN_TYPE"));
                    dbColumnInfo.setNullAble(getStringToBoolean(resultSet.getString("NULLABLE")));
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
                    if (!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")) {
                        dbColumnInfo.setAutoIncrement(true);
                    }
                    String columnKey = resultSet.getString("COLUMN_KEY");
                    if (!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")) {
                        dbColumnInfo.setPrimary(true);
                    }
                    dbColumnInfos.add(dbColumnInfo);
                }
                dbTable.setTabsColumn(dbColumnInfos);
            }
        }catch (Exception e){
            log.error("发生错误 = {}",e);
            throw e;
        }finally {
            DbConnnecttion.closeResultSet(resultSet);
            DbConnnecttion.closeStat(preparedStatement);
        }
    }

    private static boolean getStringToBoolean(final String val){
        if(org.apache.commons.lang3.StringUtils.isEmpty(val)){
            return false;
        }else{
            if("YES".equals(val)){
                return true;
            }else{
                return false;
            }
        }
    }
}
