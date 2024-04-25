package io.github.pomzwj.dbexport.core.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 类说明:
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2018/10/29/0029.
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

    public List<DbColumnInfo> getTabsColumn() {
        return tabsColumn;
    }

    public void setTabsColumn(List<DbColumnInfo> tabsColumn) {
        this.tabsColumn = tabsColumn;
    }

    public List<DbIndexInfo> getTabsIndex() {
        return tabsIndex;
    }

    public void setTabsIndex(List<DbIndexInfo> tabsIndex) {
        this.tabsIndex = tabsIndex;
    }
}
