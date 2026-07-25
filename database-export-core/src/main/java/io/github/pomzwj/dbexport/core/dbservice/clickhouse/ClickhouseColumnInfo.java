package io.github.pomzwj.dbexport.core.dbservice.clickhouse;

import io.github.pomzwj.dbexport.core.anno.DataColumnName;
import io.github.pomzwj.dbexport.core.domain.DbColumnInfo;

/**
 * clickhouse数据库列信息
 *
 * @author PomZWJ
 */
public class ClickhouseColumnInfo extends DbColumnInfo {
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
     * 备注
     */
    @DataColumnName(name = "备注", order = Integer.MAX_VALUE)
    public String comments;

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
