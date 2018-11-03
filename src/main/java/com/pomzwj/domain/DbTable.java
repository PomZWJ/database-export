package com.pomzwj.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 类说明:
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
public class DbTable implements Serializable {
    //表名
    private String tableName;
    //表注释
    private String tableComments;
    //表字段
    private List<Map> tabsColumn;

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

    public List<Map> getTabsColumn() {
        return tabsColumn;
    }

    public void setTabsColumn(List<Map> tabsColumn) {
        this.tabsColumn = tabsColumn;
    }
}
