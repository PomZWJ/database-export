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

    /**
     * 索引标题
     */
    String indexTitle;

    /**
     * 获取table
     *
     * @return table
     */
    public TableRenderData getTable() {
        return table;
    }

    /**
     * 设置table
     *
     * @param table table
     */
    public void setTable(TableRenderData table) {
        this.table = table;
    }

    /**
     * 获取tableName
     *
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置tableName
     *
     * @param tableName tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取tableComments
     *
     * @return tableComments
     */
    public String getTableComments() {
        return tableComments;
    }

    /**
     * 设置tableComments
     *
     * @param tableComments tableComments
     */
    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
    }

    /**
     * 获取indexTable
     *
     * @return indexTable
     */
    public TableRenderData getIndexTable() {
        return indexTable;
    }

    /**
     * 设置indexTable
     *
     * @param indexTable indexTable
     */
    public void setIndexTable(TableRenderData indexTable) {
        this.indexTable = indexTable;
    }

    /**
     * 获取indexTitle
     *
     * @return indexTitle
     */
    public String getIndexTitle() {
        return indexTitle;
    }

    /**
     * 设置indexTitle
     *
     * @param indexTitle indexTitle
     */
    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }
}
