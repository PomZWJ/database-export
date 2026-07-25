package io.github.pomzwj.dbexport.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC工具类，提供数据库连接关闭等功能
 *
 * @author PomZWJ
 */
public class JdbcUtils {
    static final Logger log = LoggerFactory.getLogger(JdbcUtils.class);

    /**
     * 关闭数据库连接，忽略关闭异常
     *
     * @param connection 数据库连接，为null时不处理
     */
    public static void close(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("close connection fail",e);
            }
        }
    }
}
