package com.pomzwj.dbpool;

import com.alibaba.druid.pool.DruidDataSource;
import com.pomzwj.domain.DbBaseInfo;

import javax.sql.DataSource;

/**
 * @author zhaowj
 * @date 2021-12-16
 */
public interface DbPoolService {
	/**
	 * 创建连接池
	 * @param dbBaseInfo
	 * @return
	 */
	DataSource createDbPool(DbBaseInfo dbBaseInfo);

	/**
	 * 关闭连接池
	 * @param dataSource
	 */
	void closeDbPool(DataSource dataSource);
}
