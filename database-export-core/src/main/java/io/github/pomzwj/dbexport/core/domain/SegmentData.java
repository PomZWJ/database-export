package io.github.pomzwj.dbexport.core.domain;


import com.deepoove.poi.data.TableRenderData;

import java.io.Serializable;

/**
 * 类说明:子文档的数据结构
 *
 * @author PomZWJ
 */
public class SegmentData implements Serializable {
    /**
     * 索引表结构
     */
    TableRenderData indexTable;
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

    String indexTitle;

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

    public TableRenderData getIndexTable() {
        return indexTable;
    }

    public void setIndexTable(TableRenderData indexTable) {
        this.indexTable = indexTable;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }
}
