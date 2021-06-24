package com.pomzwj.dbservice.sqlserver;

import com.deepoove.poi.data.RowRenderData;
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
import java.util.Arrays;
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
    public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();
        String jdbcStr = String.format(sqlServerJdbc,ip,port,dbName);
        Connection connection = null;
        try {
            connection = DbConnnecttion.getConn(jdbcStr, userName, password, sqlServerDriver);
            List<DbTable> tableName = this.getTableName(connection);
            this.getTabsColumnInfo(connection,tableName);
            return tableName;
        }catch (Exception e){
            log.error("发生错误 = {}",e);
            throw e;
        }finally {
            DbConnnecttion.closeConn(connection);
        }
    }

    private List<DbTable> getTableName(Connection connection) throws Exception {
        List<DbTable> tableList = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlServerGetTableNameSql);
            while (resultSet.next()) {
                DbTable dbTable = new DbTable();
                String tableName = resultSet.getString("TABLE_NAME");
                String tableComments = resultSet.getString("COMMENTS");
                if(StringUtils.isEmpty(tableComments)){
                    dbTable.setTableComments(FiledDefaultValue.TABLE_COMMENTS_DEFAULT);
                }else{
                    dbTable.setTableComments(tableComments);
                }
                dbTable.setTableName(tableName);
                tableList.add(dbTable);
            }
        } catch (Exception e) {
            log.error("获取所有表数据发生错误 = {}",e);
            throw e;
        } finally {
            DbConnnecttion.closeResultSet(resultSet);
            DbConnnecttion.closeStat(statement);
        }
        return tableList;
    }

    private void getTabsColumnInfo(Connection connection,List<DbTable> dbTableList)throws Exception {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("sql/sqlserver.sql");
            InputStream inputStream =classPathResource.getInputStream();
            if(inputStream == null){
                throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
            }
            String executeSql = IOUtils.toString(inputStream,DbService.DefaultCharsetName);

            preparedStatement = connection.prepareStatement(executeSql);
            for(int i=0;i<dbTableList.size();i++){
                List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
                DbTable dbTable = dbTableList.get(i);
                preparedStatement.setString(1,dbTable.getTableName());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
                    dbColumnInfo.setDataType(resultSet.getString("COLUMN_TYPE"));
                    dbColumnInfo.setDataLength(resultSet.getString("DATA_LENGTH"));
                    dbColumnInfo.setNullAble(getStringToBoolean(resultSet.getString("NULLABLE")));
                    dbColumnInfo.setDefaultVal(resultSet.getString("DATA_DEFAULT"));
                    dbColumnInfo.setDataScale(resultSet.getString("DATA_SCALE"));
                    String comments = resultSet.getString("COMMENTS");
                    dbColumnInfo.setAutoIncrement(getStringToBoolean(resultSet.getString("AUTOINCREMENT")));
                    dbColumnInfo.setPrimary(getStringToBoolean(resultSet.getString("PRIMARY_KEY")));
                    if (StringUtils.isEmpty(comments)) {
                        dbColumnInfo.setComments(FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                    } else {
                        dbColumnInfo.setComments(comments);
                    }
                    dbColumnInfos.add(dbColumnInfo);
                }
                dbTable.setTabsColumn(dbColumnInfos);
            }
        } catch (Exception e) {
            log.error("发生错误 = {}",e);
            throw e;
        } finally {
            DbConnnecttion.closeResultSet(resultSet);
            DbConnnecttion.closeStat(preparedStatement);
        }
    }
    private static boolean getStringToBoolean(final String val){
        if(StringUtils.isEmpty(val)){
            return false;
        }else{
            if("TRUE".equals(val)){
                return true;
            }else{
                return false;
            }
        }
    }
}
