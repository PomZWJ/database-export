package com.pomzwj.dbservice.oracle;

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
import java.util.*;

@Component
public class OracleDbService implements DbService {

    static final Logger log = LoggerFactory.getLogger(OracleDbService.class);
    @Value("${database.jdbc.oracle}")
    String oracleJdbc;
    @Value("${database.driver.oracle}")
    String oracleDriver;
    @Value("${database.getTableNameSql.oracle}")
    String oracleGetTableNameSql;


    public List<DbTable> getTableName(Connection connection) throws Exception {
        List<DbTable> tableList = new ArrayList<>();



        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(oracleGetTableNameSql);
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
            log.error("发生错误 = {}",e);
            throw e;
        } finally {
            DbConnnecttion.closeResultSet(resultSet);
            DbConnnecttion.closeStat(statement);
        }
        return tableList;
    }

    public void getTabsColumnInfo(Connection connection,String userName,List<DbTable> dbTableList) throws Exception {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("sql/oracle.sql");
            InputStream inputStream =classPathResource.getInputStream();
            if(inputStream == null){
                throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
            }
            String executeSql = IOUtils.toString(inputStream,DbService.DefaultCharsetName);

            preparedStatement = connection.prepareStatement(executeSql);
            for(int i=0;i<dbTableList.size();i++){
                DbTable dbTable = dbTableList.get(i);
                preparedStatement.setString(1,userName);
                preparedStatement.setString(2,dbTable.getTableName());
                resultSet = preparedStatement.executeQuery();
                List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
                while (resultSet.next()) {
                    DbColumnInfo dbColumnInfo = new DbColumnInfo();
                    dbColumnInfo.setColumnName(resultSet.getString("COLUMN_NAME"));
                    dbColumnInfo.setDataType(resultSet.getString("DATA_TYPE"));
                    String dataLength = resultSet.getString("DATA_LENGTH");
                    String dataPrecision = resultSet.getString("DATA_PRECISION");
                    if(StringUtils.isNotEmpty(dataPrecision)){
                        dataLength = dataPrecision;
                    }
                    dbColumnInfo.setDataLength(dataLength);
                    dbColumnInfo.setDataScale(resultSet.getString("DATA_SCALE"));
                    dbColumnInfo.setNullAble(getStringToBoolean(resultSet.getString("NULLABLE")));
                    dbColumnInfo.setDefaultVal(resultSet.getString("DATA_DEFAULT"));
                    String comments = resultSet.getString("COMMENTS");
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
        if(org.apache.commons.lang3.StringUtils.isEmpty(val)){
            return false;
        }else{
            if("Y".equals(val)){
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception {
        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();
        String jdbcStr = String.format(oracleJdbc,ip,port,dbName);
        Connection connection = null;
        try {
            connection = DbConnnecttion.getConn(jdbcStr, userName, password, oracleDriver);
            List<DbTable> tableName = this.getTableName(connection);
            this.getTabsColumnInfo(connection,userName,tableName);
            return tableName;
        }catch (Exception e){
            log.error("发生错误 = {}",e);
            throw e;
        }finally {
            DbConnnecttion.closeConn(connection);
        }
    }
}
