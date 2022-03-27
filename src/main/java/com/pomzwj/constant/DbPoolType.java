package com.pomzwj.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据库连接池类型
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public enum DbPoolType {
	DRUID("druid"),
	HIKARICP("hikaricp")
	;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	DbPoolType(String name) {
		this.name = name;
	}
	public static DbPoolType matchType(String name){
		if (StringUtils.isNotEmpty(name)) {
			for (DbPoolType dbPoolType: DbPoolType.values()) {
				if (dbPoolType.name().equals(name.toUpperCase())) {
					return dbPoolType;
				}
			}
		}
		return null;
	}
}
