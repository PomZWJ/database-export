package io.github.pomzwj.dbexport.web;

import io.github.pomzwj.dbexport.core.DataBaseExportExecute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @describe 数据库导出工具启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DatabaseExportApplication {

	public static void main(String[] args) {
        SpringApplication.run(DatabaseExportApplication.class, args);
	}

    @Bean
    public DataBaseExportExecute dataBaseExportExecute(){
        return new DataBaseExportExecute();
    }
}
