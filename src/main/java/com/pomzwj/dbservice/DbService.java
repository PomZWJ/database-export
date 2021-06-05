package com.pomzwj.dbservice;

import com.pomzwj.domain.DbBaseInfo;
import com.pomzwj.domain.DbColumnInfo;
import com.pomzwj.domain.DbTable;
import com.pomzwj.domain.TempData;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库服务接口
 */
public interface DbService {
    String DefaultCharsetName = "UTF-8";


    /**
     * 定义列名
     * @return
     */
    List<String> initRowName();


    /**
     * 获取temp数据
     * @return
     */
    List<TempData> getWordTempData(List<DbTable> tableMessage);

    /**
     * 获取表信息
     * @param dbBaseInfo
     * @return
     */
    List<DbTable> getTableName(DbBaseInfo dbBaseInfo)throws Exception;

    /**
     * 获取所有的字段信息
     * @param dbTableList
     * @return
     */
    void getTabsColumnInfo(DbBaseInfo dbBaseInfo,List<DbTable>dbTableList)throws Exception;



}
