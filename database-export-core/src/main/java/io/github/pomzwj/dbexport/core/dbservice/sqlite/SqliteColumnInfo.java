package io.github.pomzwj.dbexport.core.dbservice.sqlite;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;

/**
 * sqlite数据库列信息
 *
 * @author PomZWJ
 */
public class SqliteColumnInfo extends DbColumnInfo {
    /**
     * 是否为空
     */
    @DataColumnName(name = "是否为空", order = 2)
    public Boolean nullAble;
    /**
     * 默认值
     */
    @DataColumnName(name = "默认值",order = 3)
    public String defaultVal;
    /**
     * 主键
     */
    @DataColumnName(name = "主键",order = 4)
    public Boolean primary;
    /**
     * 备注
     */
    @DataColumnName(name = "备注", order = Integer.MAX_VALUE)
    public String comments;

    /**
     * 获取defaultVal
     *
     * @return defaultVal
     */
    public String getDefaultVal() {
        return defaultVal;
    }

    /**
     * 设置defaultVal
     *
     * @param defaultVal defaultVal
     */
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    /**
     * 获取primary
     *
     * @return primary
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     * 设置primary
     *
     * @param primary primary
     */
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    /**
     * 获取nullAble
     *
     * @return nullAble
     */
    public Boolean getNullAble() {
        return nullAble;
    }

    /**
     * 设置nullAble
     *
     * @param nullAble nullAble
     */
    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    /**
     * 获取comments
     *
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * 设置comments
     *
     * @param comments comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
