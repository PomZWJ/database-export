package com.pomzwj.service.impl;

import com.pomzwj.constant.DbExportConstants;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.service.IDataOperatorService;
import com.pomzwj.utils.DbConnnecttion;
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
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
@Service
public class DataOperatorServiceImpl implements IDataOperatorService {


    @Override
    public List<DbTable> getTablename(String dbKind, DbBaseInfo info) throws Exception {
        List<DbTable> tableList = new ArrayList<>();
        Connection connection = DbConnnecttion.getConn(DbExportConstants.getJdbcUrl(dbKind,info.getIp(),info.getPort(),info.getDbName()), info.getUserName(), info.getPassword(), DbExportConstants.getDriverClassName(dbKind));
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(DbExportConstants.getTableNameSQL(dbKind,info.getDbName()));
        while (resultSet.next()) {
            DbTable dbTable = new DbTable();
            String tableName = resultSet.getString("TABLE_NAME");
            String tableComments = resultSet.getString("COMMENTS");
            dbTable.setTableComments(tableComments);
            dbTable.setTableName(tableName);
            tableList.add(dbTable);
        }

        for(int i = 0;i<tableList.size() ; i++){
            DbTable dbTable = tableList.get(i);
            List<Map> tabsColumn = this.getTabsColumn(dbKind,dbTable.getTableName(), connection);
            dbTable.setTabsColumn(tabsColumn);
        }
        DbConnnecttion.closeConn(connection);
        return tableList;
    }

    @Override
    public List<Map> getTabsColumn(String dbKind , String tableName, Connection connection) throws Exception {
        List<Map>list=new ArrayList<>();

        Statement statement = connection.createStatement();
        String sql= DbExportConstants.getColNameInfoSQL(dbKind,tableName);
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Map<String,String>colDataMap=new HashMap<>();
            colDataMap.put("COLUMN_NAME" , resultSet.getString("COLUMN_NAME"));
            colDataMap.put("DATA_TYPE",resultSet.getString("DATA_TYPE"));
            colDataMap.put("DATA_LENGTH", resultSet.getString("DATA_LENGTH"));
            colDataMap.put("NULL_ABLE", resultSet.getString("NULLABLE"));
            colDataMap.put("DATA_DEFAULT", resultSet.getString("DATA_DEFAULT"));
            colDataMap.put("COMMENTS",resultSet.getString("COMMENTS"));
            list.add(colDataMap);
        }
        return list;
    }


}
