package com.pomzwj.domain;

import com.deepoove.poi.data.MiniTableRenderData;

import java.io.Serializable;

/**
 * 类说明:子文档的数据结构
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
public class SegmentData implements Serializable {
    /**
     * 表结构
     */
    MiniTableRenderData table;
    /**
     * 表名
     */
    String tableName;

    /**
     * 表注释
     */
    String tableComments;

    public MiniTableRenderData getTable() {
        return table;
    }

    public void setTable(MiniTableRenderData table) {
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
