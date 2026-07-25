package io.github.pomzwj.dbexport.core.dbservice.sqlserver;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

/**
 * sqlserver数据库索引信息
 *
 * @author PomZWJ
 */
public class SqlServerIndexInfo extends DbIndexInfo {
    /**
     * 索引名称
     */
    @DbIndexName(name = "索引名称", order = 0)
    public String name;
    /**
     * 索引类型
     */
    @DbIndexName(name = "索引类型", order = 2)
    public String indexType;
    /**
     * 索引序列
     */
    @DbIndexName(name = "索引序列", order = 3)
    public Integer indexId;
    /**
     * 索引升序
     */
    @DbIndexName(name = "索引升序", order = 4)
    public Boolean descending;

    /**
     * 获取name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取indexType
     *
     * @return indexType
     */
    public String getIndexType() {
        return indexType;
    }

    /**
     * 设置indexType
     *
     * @param indexType indexType
     */
    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    /**
     * 获取indexId
     *
     * @return indexId
     */
    public Integer getIndexId() {
        return indexId;
    }

    /**
     * 设置indexId
     *
     * @param indexId indexId
     */
    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    /**
     * 获取descending
     *
     * @return descending
     */
    public Boolean getDescending() {
        return descending;
    }

    /**
     * 设置descending
     *
     * @param descending descending
     */
    public void setDescending(Boolean descending) {
        this.descending = descending;
    }
}
