package io.github.pomzwj.dbexport.core;

import com.google.gson.Gson;
import io.github.pomzwj.dbexport.core.utils.JdbcUrlParseUtils;
import org.junit.jupiter.api.Test;

public class JdbcUrlParseTest {

    @Test
    public void test()throws Exception{
        Gson gson = new Gson();
        String mysqlUrl = "jdbc:mysql://192.168.1.1:3306/demo_db?useUnicode=true&zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8&useSSL=false";
        String oracleUrl = "jdbc:oracle:thin:@//192.168.1.1:3306/orcl";
        String sqlServerUrl = "jdbc:sqlserver://192.168.1.1:3306;databaseName=demo_db";
        String postgresqlUrl = "jdbc:postgresql://192.168.1.1:3306/demo_db?currentSchema=public";
        String clickhouseUrl = "jdbc:clickhouse://192.168.1.1:3306/demo_db";
        String sqliteUrl = "jdbc:sqlite:D:\\kk\\demo_db.db";
        String dmUrl = "jdbc:dm://192.168.1.1:3306/demo_db";
        String db2Url = "jdbc:db2://192.168.1.1:3306/demo_db:currentSchema=ADMINISTRATOR;";

        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(mysqlUrl)));
        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(oracleUrl)));
        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(sqlServerUrl)));
        System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(postgresqlUrl)));
        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(clickhouseUrl)));
        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(sqliteUrl)));
        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(dmUrl)));
        //System.out.println(gson.toJson(JdbcUrlParseUtils.parseJdbcUrlStringToBaseInfo(db2Url)));
    }

}
