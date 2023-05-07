package com.pomzwj.dbservice;

import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbTable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 数据库服务接口
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public interface DbService {
	String DefaultCharsetName = "UTF-8";


    /**
     * 获取数据库表的详细信息
     * @param dbBaseInfo
     * @return
     * @throws Exception
     */
	public List<DbTable> getTableDetailInfo(DbBaseInfo dbBaseInfo) throws Exception;

}
