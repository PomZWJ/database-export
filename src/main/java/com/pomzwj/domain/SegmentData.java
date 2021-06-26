package com.pomzwj.domain;


import com.deepoove.poi.data.TableRenderData;

import java.io.Serializable;

/**
 * 类说明:子文档的数据结构
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2018/10/29/0029.
 */
public class SegmentData implements Serializable {
    /**
     * 表结构
     */
    TableRenderData table;
    /**
     * 表名
     */
    String tableName;

    /**
     * 表注释
     */
    String tableComments;

    public TableRenderData getTable() {
        return table;
    }

    public void setTable(TableRenderData table) {
        this.table = table;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComments() {
        return tableComments;
    }

    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
    }
}
