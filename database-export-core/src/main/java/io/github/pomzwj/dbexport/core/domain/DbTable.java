package io.github.pomzwj.dbexport.core.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 类说明:数据库表信息
 *
 * @author PomZWJ
 */
public class DbTable implements Serializable {
    //表名
    private String tableName;
    //表注释
    private String tableComments;
    //表字段
    private List<DbColumnInfo> tabsColumn;
    //表索引
    private List<DbIndexInfo> tabsIndex;


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
     * 获取tabsColumn
     *
     * @return tabsColumn
     */
    public List<DbColumnInfo> getTabsColumn() {
        return tabsColumn;
    }

    /**
     * 设置tabsColumn
     *
     * @param tabsColumn tabsColumn
     */
    public void setTabsColumn(List<DbColumnInfo> tabsColumn) {
        this.tabsColumn = tabsColumn;
    }

    /**
     * 获取tabsIndex
     *
     * @return tabsIndex
     */
    public List<DbIndexInfo> getTabsIndex() {
        return tabsIndex;
    }

    /**
     * 设置tabsIndex
     *
     * @param tabsIndex tabsIndex
     */
    public void setTabsIndex(List<DbIndexInfo> tabsIndex) {
        this.tabsIndex = tabsIndex;
    }
}
