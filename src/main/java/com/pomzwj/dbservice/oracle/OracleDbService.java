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


    @Override
    public List<String> initRowName() {
        List<String>rowNames = Arrays.asList("列名", "数据类型","数据长度","精度","是否为空", "默认值", "备注");
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
                String columnName = dbColumnInfo.getColumnName();
                //数据类型
                String dataType = dbColumnInfo.getDataType();
                //数据长度
                String dataLength = dbColumnInfo.getDataLength();
                //精度
                String dataScale = dbColumnInfo.getDataScale();
                //是否可空
                Boolean nullAble = dbColumnInfo.getNullAble();
                //数据缺省值
                String dataDefault = dbColumnInfo.getDefaultVal();
                //字段注释
                String comments = dbColumnInfo.getComments();

                RowRenderData labor = RowRenderData.build( columnName, dataType,dataLength,dataScale,nullAble?"是":"否",dataDefault,comments);
                rowRenderDataList.add(labor);
            }
            tempData.setData(rowRenderDataList);
            tempDataList.add(tempData);
        }
        return tempDataList;
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
            Connection connection = DbConnnecttion.getConn(String.format(oracleJdbc,ip,port,dbName), userName, password, oracleDriver);
            Statement statement = connection.createStatement();
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


        ResultSet resultSet = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("sql/oracle.sql");
            InputStream inputStream =classPathResource.getInputStream();
            if(inputStream == null){
                throw new FileNotFoundException("没有找到查询详细字段的SQL文件");
            }
            String executeSql = IOUtils.toString(inputStream,DbService.DefaultCharsetName);
            String jdbcStr = String.format(oracleJdbc,ip,port,dbName);
            Connection connection = DbConnnecttion.getConn(jdbcStr, userName, password, oracleDriver);

            PreparedStatement preparedStatement = connection.prepareStatement(executeSql);
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
            DbConnnecttion.closeRs(resultSet);
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

}
