package com.pomzwj.dbservice.mysql;

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
import java.util.Objects;

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
    public List<String> initRowName() {
        List<String>rowNames = Arrays.asList("列名", "数据类型","是否为空","主键","自增", "默认值", "备注");
        return rowNames;
    }

    @Override
    public List<TempData> getWordTempData(List<DbTable> tableMessage) {
        List<TempData>tempDataList=new ArrayList<>();
        for (DbTable dbTable : tableMessage) {
            List<DbColumnInfo> data =  dbTable.getTabsColumn();
            TempData tempData = new TempData();
            tempData.setTableComment(dbTable.getTableComments());
            tempData.setTableName(dbTable.getTableName());

            List<RowRenderData> rowRenderDataList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {

                DbColumnInfo dbColumnInfo = data.get(i);
                //列名
                String column_name = dbColumnInfo.getColumnName();
                //数据类型
                String data_type = dbColumnInfo.getDataType();
                //是否可空
                Boolean nullAble = dbColumnInfo.getNullAble();
                //数据缺省值
                String data_default = dbColumnInfo.getDefaultVal();
                //字段注释
                String comments = dbColumnInfo.getComments();
                Boolean autoIncrement = dbColumnInfo.getAutoIncrement();
                String auto_increment = autoIncrement?"是":"";

                Boolean primary = dbColumnInfo.getPrimary();
                String is_primary = primary?"是":"";

                String null_able = nullAble?"是":"否";

                RowRenderData labor = RowRenderData.build( column_name, data_type,null_able,is_primary,auto_increment,data_default,comments);
                rowRenderDataList.add(labor);
            }
            tempData.setData(rowRenderDataList);
            tempDataList.add(tempData);
        }
        return tempDataList;
    }

    @Override
    public List<DbTable> getTableName(DbBaseInfo dbBaseInfo)throws Exception {
        List<DbTable> tableList = new ArrayList<>();

        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();

        ResultSet resultSet = null;
        try {

            String jdbcStr = String.format(mysqlJdbc,ip,port,dbName);
            Connection connection = DbConnnecttion.getConn(jdbcStr, userName, password, mysqlDriver);

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(mysqlGetTableNameSql,dbName));
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
            DbConnnecttion.closeRs(resultSet);
        }
        return tableList;
    }

    @Override
    public void getTabsColumnInfo(DbBaseInfo dbBaseInfo,List<DbTable> dbTableList)throws Exception {
        String dbName = dbBaseInfo.getDbName();
        String ip = dbBaseInfo.getIp();
        String port = dbBaseInfo.getPort();
        String userName = dbBaseInfo.getUserName();
        String password = dbBaseInfo.getPassword();

        ResultSet resultSet = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("sql/mysql.sql");
            InputStream inputStream =classPathResource.getInputStream();
            if(inputStream == null){
                throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
            }
            String executeSql = IOUtils.toString(inputStream,DbService.DefaultCharsetName);
            String jdbcStr = String.format(mysqlJdbc,ip,port,dbName);
            Connection connection = DbConnnecttion.getConn(jdbcStr, userName, password, mysqlDriver);

            PreparedStatement preparedStatement = connection.prepareStatement(executeSql);
            for(int i=0;i<dbTableList.size();i++){
                List<DbColumnInfo> dbColumnInfos = new ArrayList<>();
                DbTable dbTable = dbTableList.get(i);
                preparedStatement.setString(1,dbTable.getTableName());
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
                    if(!StringUtils.isEmpty(extraInfo) && extraInfo.contains("auto_increment")){
                        dbColumnInfo.setAutoIncrement(true);
                    }
                    String columnKey = resultSet.getString("COLUMN_KEY");
                    if(!StringUtils.isEmpty(columnKey) && columnKey.contains("PRI")){
                        dbColumnInfo.setPrimary(true);
                    }
                    dbColumnInfos.add(dbColumnInfo);
                }
                dbTable.setTabsColumn(dbColumnInfos);
            }
        } catch (Exception e) {
            log.error("发生错误 = {}",e);
            throw e;
        } finally {
            DbConnnecttion.closeRs(resultSet);
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
