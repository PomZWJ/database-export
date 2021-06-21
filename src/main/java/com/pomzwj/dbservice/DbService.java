package com.pomzwj.dbservice;

import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;

import java.util.List;

/**
 * 数据库服务接口
 */
public interface DbService {
	String DefaultCharsetName = "UTF-8";


    /**
     * 获取数据库表的详细信息
     * @param dbBaseInfo
     * @return
     * @throws Exception
     */
	List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception;

}
