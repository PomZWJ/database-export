package io.github.pomzwj.dbexport.core.domain;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;

/**
 * 类说明:数据库索引信息
 *
 * @author PomZWJ
 */
public class DbIndexInfo {
    /**
     * 索引字段
     */
    @DbIndexName(name="索引字段",order=1)
    public String fields;

    /**
     * 获取fields
     *
     * @return fields
     */
    public String getFields() {
        return fields;
    }

    /**
     * 设置fields
     *
     * @param fields fields
     */
    public void setFields(String fields) {
        this.fields = fields;
    }
}
