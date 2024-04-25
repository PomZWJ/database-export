package io.github.pomzwj.dbexport.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcUtils {
    static final Logger log = LoggerFactory.getLogger(JdbcUtils.class);

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
