package com.pomzwj.service.impl;

import com.pomzwj.constant.DbExportConstants;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.service.IDataOperatorService;
import com.pomzwj.utils.DbConnnecttion;
import com.pomzwj.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类说明:数据操作类
 *
 * @author zhaowenjie<1 5 1 3 0 4 1 8 2 0 @ qq.com>
 * @date 2018/10/29/0029.
 */
@Service
public class DataOperatorServiceImpl implements IDataOperatorService {


    @Override
    public List<DbTable> getTableName(String dbKind, DbBaseInfo info) throws Exception {
        List<DbTable> tableList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbConnnecttion.getConn(DbExportConstants.getJdbcUrl(dbKind, info.getIp(), info.getPort(), info.getDbName()), info.getUserName(), info.getPassword(), DbExportConstants.getDriverClassName(dbKind));
            statement = connection.createStatement();
            resultSet = statement.executeQuery(DbExportConstants.getTableNameSQL(dbKind, info.getDbName()));
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

            for (int i = 0; i < tableList.size(); i++) {
                DbTable dbTable = tableList.get(i);
                List<Map> tabsColumn = this.getTabsColumn(dbKind, dbTable.getTableName(), connection);
                dbTable.setTabsColumn(tabsColumn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                DbConnnecttion.closeRs(resultSet);
            }
            if(statement != null){
                DbConnnecttion.closeStat(statement);
            }
            if(connection != null){
                DbConnnecttion.closeConn(connection);
            }

        }
        return tableList;
    }

    @Override
    public List<Map> getTabsColumn(String dbKind, String tableName, Connection connection) throws Exception {
        List<Map> list = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = DbExportConstants.getColNameInfoSQL(dbKind, tableName);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Map<String, String> colDataMap = new HashMap<>();
                colDataMap.put("COLUMN_NAME", resultSet.getString("COLUMN_NAME"));
                colDataMap.put("DATA_TYPE", resultSet.getString("DATA_TYPE"));
                colDataMap.put("DATA_LENGTH", resultSet.getString("DATA_LENGTH"));
                colDataMap.put("NULL_ABLE", resultSet.getString("NULLABLE"));
                colDataMap.put("DATA_DEFAULT", resultSet.getString("DATA_DEFAULT"));
                String comments = resultSet.getString("COMMENTS");
                if (!StringUtils.isEmpty(comments)) {
                    colDataMap.put("COMMENTS", comments);
                } else {
                    colDataMap.put("COMMENTS", FiledDefaultValue.TABLE_FIELD_COMMENTS_DEFAULT);
                }

                list.add(colDataMap);
            }
        }catch (Exception e){
           throw e;
        }finally {
            if(resultSet != null){
                DbConnnecttion.closeRs(resultSet);
            }
            if(statement != null){
                DbConnnecttion.closeStat(statement);
            }
        }
        return list;
    }


}
