package io.github.pomzwj.dbexport.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author PomZWJ
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DatabaseExportApplication {

	public static void main(String[] args) {
        SpringApplication.run(DatabaseExportApplication.class, args);
	}
}
