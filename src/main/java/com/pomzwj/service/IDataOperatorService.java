package com.pomzwj.service;

import com.pomzwj.domain.DbBaseInfo;
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
    public List<DbTable> getTablename (String dbKind, DbBaseInfo info) throws Exception;
    public List<Map>getTabsColumn(String dbKind , String tableName , Connection connection)throws Exception;
}
