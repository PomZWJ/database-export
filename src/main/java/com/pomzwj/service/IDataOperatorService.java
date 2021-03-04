package com.pomzwj.service;

import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 类说明:数据操作服务接口
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
public interface IDataOperatorService {
    List<DbTable> getTableName (DbBaseInfo info) throws Exception;
    List<DbColumnInfo>getTabsColumn(String dbKind , String tableName , Connection connection)throws Exception;
}
