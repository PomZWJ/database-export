package com.pomzwj.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.stereotype.Component;

/**
 * 类说明:Druid连接池
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
@Component
public class DruidUtils {

    public DruidDataSource getDataSource(String jdbcUrl,String userName,String password,String driverClassName){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setMaxWait(10000L);
        dataSource.setInitialSize(8);
        dataSource.setTimeBetweenEvictionRunsMillis(3000);
        return dataSource;
    }













}
