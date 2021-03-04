package com.pomzwj.service.impl;

import com.pomzwj.constant.DbExportConstants;
import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.FiledDefaultValue;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.service.IDataOperatorService;
import com.pomzwj.utils.DbConnnecttion;
import com.pomzwj.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author zhaowenjie<1513041820@ qq.com>
 * @date 2018/10/29/0029.
 */
@Slf4j
@Service
public class DataOperatorServiceImpl implements IDataOperatorService {

    @Autowired
    private DbExportConstants dbExportConstants;

    @Override
    public List<DbTable> getTableName(DbBaseInfo info) throws Exception {
        String dbKind = info.getDbKind();
        String dbName = info.getDbName();
        String ip = info.getIp();
        String port = info.getPort();
        String userName = info.getUserName();
        String password = info.getPassword();

        List<DbTable> tableList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            String jdbcUrl = dbExportConstants.getJdbcUrl(dbKind, ip, port, dbName);
            Connection connection = DbConnnecttion.getConn(jdbcUrl, userName, password, dbExportConstants.getDriverClassName(dbKind));
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(dbExportConstants.getTableNameSql(dbKind, dbName));
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

            for (int i = 0; i < tableList.size(); i++) {
                DbTable dbTable = tableList.get(i);
                List<DbColumnInfo> tabsColumn = this.getTabsColumn(dbKind, dbTable.getTableName(), connection);
                dbTable.setTabsColumn(tabsColumn);
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
    public List<DbColumnInfo> getTabsColumn(String dbKind, String tableName, Connection connection) throws Exception {
        List<DbColumnInfo> list = new ArrayList<>();
        ResultSet resultSet = null;
        String sql = dbExportConstants.getColumnNameInfoSQL(dbKind, tableName);
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
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
           throw e;
        }
        return list;
    }


}
