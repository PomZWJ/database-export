package io.github.pomzwj.dbexport.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant;
import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.domain.DbTable;
import io.github.pomzwj.dbexport.core.type.ExportFileType;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant.SYSTEM_FILE_DIR;

public class MySqlTest {
    //生成文件的默认保存地址
    public final static String GENERATION_FILE_TEMP_DIR = SYSTEM_FILE_DIR + File.separator + "fileTemp";
    protected DataSource getClickHouseDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:clickhouse://192.168.0.30:8123/zetag_server_db");
        dataSource.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver");
        dataSource.setUsername("default");
        dataSource.setPassword("zifisenseck");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getMySqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/demo_db?useUnicode=true&zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8&useSSL=false");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getPostgreSqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:postgresql://192.168.1.103:5432/demo_db?currentSchema=public");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getSqliteDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlite:D:/my/sqlite/information.db");
        dataSource.setDriverClassName("org.sqlite.JDBC");
        //dataSource.setUsername("postgres");
        //dataSource.setPassword("123456");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getDmDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:dm://127.0.0.1:5236/DMHR");
        dataSource.setDriverClassName("dm.jdbc.driver.DmDriver");
        dataSource.setUsername("SYSDBA");
        dataSource.setPassword("123456789");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getSqlServerDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlserver://192.168.1.102:1433;databaseName=demo_db;encrypt=false");
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUsername("sa");
        dataSource.setPassword("zwj\"123456");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getOracleDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@//192.168.1.103:1521/onepay");
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUsername("hr");
        dataSource.setPassword("123456");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected DataSource getDb2DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:db2://192.168.1.102:50000/sample:currentSchema=ADMINISTRATOR;");
        dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
        dataSource.setUsername("administrator");
        dataSource.setPassword("zwj@123456");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxWait(60 * 1000);
        dataSource.setMaxActive(20);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(10);
        return dataSource;
    }

    protected static DbExportConfig getConfig() {
        return new DbExportConfig()
                .setSearchIndex(true)
                .setExportFileTypeEnum(ExportFileType.WORD)
                .setGenerationFileTempDir(GENERATION_FILE_TEMP_DIR)
                //.setShowColumnList(Lists.newArrayList("columnName","dataType","autoIncrement"))
                //.setShowIndexList(Lists.newArrayList("name","seqIndex"))
                ;
    }

    @Test
    public void getTable(){
        DataSource dataSource = getPostgreSqlDataSource();
        try {
            List<DbTable> dbTables = DataBaseExportExecute.executeGetTableAndComments(dataSource, getConfig());
            Gson gson = new GsonBuilder().serializeNulls().create();
            System.out.println(gson.toJson(dbTables));
        } finally {
            if (dataSource != null) {
                ((DruidDataSource) dataSource).close();
            }
        }
    }
    @Test
    public void getTableDetail()throws Exception{
        DataSource dataSource = getPostgreSqlDataSource();
        try {
            List<DbTable> dbTables = DataBaseExportExecute.executeGetTableDataAll(dataSource, getConfig());
            Gson gson = new GsonBuilder().serializeNulls().create();
            System.out.println(gson.toJson(dbTables));
        } finally {
            if (dataSource != null) {
                ((DruidDataSource) dataSource).close();
            }
        }
    }

    @Test
    public void makeFile()throws Exception{
        DataSource dataSource = getMySqlDataSource();
        try {
            DataBaseExportExecute.executeFile(dataSource, getConfig());
        } finally {
            if (dataSource != null) {
                ((DruidDataSource) dataSource).close();
            }
        }
    }
}
