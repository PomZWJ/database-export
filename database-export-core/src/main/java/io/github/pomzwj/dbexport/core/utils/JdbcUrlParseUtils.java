package io.github.pomzwj.dbexport.core.utils;

import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.domain.DbBaseInfo;
import io.github.pomzwj.dbexport.core.type.DataBaseType;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC连接URL解析工具类，从数据源或连接中解析出数据库基础信息
 *
 * @author PomZWJ
 */
public class JdbcUrlParseUtils {

    /**
     * 从数据源解析连接URL为数据库基础信息
     *
     * @param dataSource 数据源
     * @return 数据库基础信息
     */
    public static DbBaseInfo parseConnectionUrlToBaseInfo(DataSource dataSource){
        try {
            return parseConnectionUrlToBaseInfo(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从连接中解析连接URL为数据库基础信息
     *
     * @param connection 数据库连接
     * @return 数据库基础信息
     */
    public static DbBaseInfo parseConnectionUrlToBaseInfo(Connection connection){
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String userName = metaData.getUserName();
            String url = metaData.getURL();
            DbBaseInfo dbBaseInfo = parseJdbcUrlStringToBaseInfo(url);
            if(StringUtils.isNoneBlank(userName)){
                String[] split = userName.split("@");
                dbBaseInfo.setUserName(split[0]);
                if(split.length > 1){
                    dbBaseInfo.setIp(split[1]);
                }
            }
            return dbBaseInfo;
        } catch (SQLException | UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }

    /**
     * 解析JDBC连接字符串为数据库基础信息
     *
     * @param url JDBC连接字符串
     * @return 数据库基础信息
     * @throws UnsupportedEncodingException 当URL解码编码不支持时抛出
     */
    public static DbBaseInfo parseJdbcUrlStringToBaseInfo(String url) throws UnsupportedEncodingException {
        DbBaseInfo dbBaseInfo = new DbBaseInfo();
        String[] split = url.split(":");
        String dbType = split[1];
        dbBaseInfo.setDbKindEnum(DataBaseType.matchType(dbType));

        if(dbBaseInfo.getDbKindEnum() == DataBaseType.SQLITE){
            String[] slashSplit = url.split("[/\\\\]");
            dbBaseInfo.setDbName(slashSplit[slashSplit.length-1]);
        }else if(dbBaseInfo.getDbKindEnum() == DataBaseType.SQLSERVER){
            Map<String, String> queryPramsMap = parseSqlServerParamsToMap(url);
            dbBaseInfo.setDbName(queryPramsMap.get("databaseName".toUpperCase()));
        }else if(dbBaseInfo.getDbKindEnum() == DataBaseType.POSTGRESQL){
            Map<String, String> queryPramsMap = parsePostgreSqlParamsToMap(url);
            String u1 = queryPramsMap.get("currentSchema".toUpperCase());
            String u2 = queryPramsMap.get("searchpath".toUpperCase());
            if(StringUtils.isEmpty(u1) && StringUtils.isEmpty(u2)){
                dbBaseInfo.setSchemas("public");
            }else{
                dbBaseInfo.setSchemas(StringUtils.isEmpty(u1)?u2:u1);
            }
            String[] parts = url.split("/"); // 使用"/"分割字符串
            String[] params = parts[3].split("\\?");
            dbBaseInfo.setDbName(params[0]); // 服务名
        }else if(dbBaseInfo.getDbKindEnum() == DataBaseType.DB2){
            String[] parts = url.split("/"); // 使用"/"分割字符串
            String[] params = parts[3].split("\\?");
            dbBaseInfo.setDbName(params[0].split(":")[0]); // 服务名
            Map<String, String> queryPramsMap = parseDb2ParamsToMap(params[0]);
            String schema = queryPramsMap.get("currentSchema".toUpperCase());
            dbBaseInfo.setSchemas(StringUtils.isNotEmpty(schema)?schema:"CURRENT SCHEMA");
        }else{
            String[] parts = url.split("/"); // 使用"/"分割字符串
            String[] params = parts[3].split("\\?");
            dbBaseInfo.setDbName(params[0]); // 服务名
        }
        return dbBaseInfo;
    }

    private static Map<String, String> splitJdbcUrlQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> queryPairs = new HashMap<>();
        if (StringUtils.isBlank(query)) {
            return queryPairs;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = URLDecoder.decode(pair.substring(0, idx), DataBaseConfigConstant.DEFAULT_ENCODE);
            String value = URLDecoder.decode(pair.substring(idx + 1), DataBaseConfigConstant.DEFAULT_ENCODE);
            queryPairs.put(key.toUpperCase(), value);
        }
        return queryPairs;
    }

    private static Map<String, String> parseSqlServerParamsToMap(String url) {
        Map<String, String> params = new HashMap<>();
        if (url != null && url.contains(";")) {
            String query = url.substring(url.indexOf(";") + 1);
            for (String param : query.split(";")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length > 1) {
                    params.put(keyValue[0].toUpperCase(), keyValue[1]);
                } else if (keyValue.length == 1 && param.indexOf('=') == param.length() - 1) {
                    params.put(keyValue[0].toUpperCase(), "");
                }
            }
        }
        return params;
    }
    private static Map<String, String> parsePostgreSqlParamsToMap(String url) {
        Map<String, String> params = new HashMap<>();
        if (url != null && url.contains("?")) {
            String query = url.substring(url.indexOf("?") + 1);
            for (String param : query.split(";")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length > 1) {
                    params.put(keyValue[0].toUpperCase(), keyValue[1]);
                } else if (keyValue.length == 1 && param.indexOf('=') == param.length() - 1) {
                    params.put(keyValue[0].toUpperCase(), "");
                }
            }
        }
        return params;
    }
    /**
     * 解析DB2连接字符串中的参数为键值对Map
     *
     * @param url DB2连接字符串
     * @return 参数键值对Map，键已转换为大写
     */
    public static Map<String, String> parseDb2ParamsToMap(String url){
        Map<String, String> params = new HashMap<>();
        if (url != null && url.contains(":")) {
            String query = url.substring(url.indexOf(":") + 1);
            for (String param : query.split(";")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length > 1) {
                    params.put(keyValue[0].toUpperCase(), keyValue[1]);
                } else if (keyValue.length == 1 && param.indexOf('=') == param.length() - 1) {
                    params.put(keyValue[0].toUpperCase(), "");
                }
            }
        }
        return params;
    }
}
