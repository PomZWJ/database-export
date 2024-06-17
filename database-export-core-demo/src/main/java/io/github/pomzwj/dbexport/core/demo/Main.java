package io.github.pomzwj.dbexport.core.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import io.github.pomzwj.dbexport.core.DataBaseExportExecute;
import io.github.pomzwj.dbexport.core.domain.DbExportConfig;
import io.github.pomzwj.dbexport.core.type.ExportFileType;

import javax.sql.DataSource;
import java.io.File;

import static io.github.pomzwj.dbexport.core.constant.DataBaseConfigConstant.SYSTEM_FILE_DIR;

public class Main {


    //生成文件的默认保存地址
    public final static String GENERATION_FILE_TEMP_DIR = SYSTEM_FILE_DIR + File.separator + "fileTemp";
    protected static DataSource getMySqlDataSource() {
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
    protected static DbExportConfig getConfig() {
        return new DbExportConfig()
                .setSearchIndex(true)
                .setExportFileTypeEnum(ExportFileType.WORD)
                .setGenerationFileTempDir(GENERATION_FILE_TEMP_DIR)
                .setShowColumnList(Lists.newArrayList("columnName","dataType","autoIncrement"))
                .setShowIndexList(Lists.newArrayList("name","seqIndex"))
                ;
    }

    /**
     * 这边仅仅展示Mysql示例,其他数据库的示例请查看database-export-core#test下的MySqlTest
     * @param args
     */
    public static void main(String[] args){
        DataSource dataSource = getMySqlDataSource();
        try {
            DataBaseExportExecute.executeFile(dataSource, getConfig());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (dataSource != null) {
                ((DruidDataSource) dataSource).close();
            }
        }
    }
}
