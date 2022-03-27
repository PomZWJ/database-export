package com.pomzwj.dbpool;

import com.pomzwj.constant.DataBaseType;
import com.pomzwj.constant.DbPoolType;
import com.pomzwj.dbpool.druid.DruidPoolUtils;
import com.pomzwj.dbpool.hikaricp.HikariCpPoolUtils;
import com.pomzwj.dbservice.DbService;
import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;
import com.pomzwj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据库连接池配置
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2021-12-16
 */
@Component
public class DbPoolFactory {
	@Value("${database.pool:druid}")
	private String databasePoolType;

	@Autowired
	private DruidPoolUtils druidPoolUtils;
	@Autowired
	private HikariCpPoolUtils hikariCpPoolUtils;
	public DbPoolService getDbPoolServiceBean() {
		if (DbPoolType.DRUID.name().equals(databasePoolType.toUpperCase())) {
			return druidPoolUtils;
		}else if(DbPoolType.HIKARICP.name().equals(databasePoolType.toUpperCase())){
			return hikariCpPoolUtils;
		}
		return null;
	}
}
