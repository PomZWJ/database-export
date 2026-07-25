package io.github.pomzwj.dbexport.core.dbservice.mysql;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

/**
 * mysql数据库索引信息
 *
 * @author PomZWJ
 */
public class MySqlIndexInfo extends DbIndexInfo {
    /**
     * 索引名称
     */
    @DbIndexName(name="索引名称",order=0)
    public String name;
    /**
     * 索引类型
     */
    @DbIndexName(name="索引类型",order=2)
    public String type;
    /**
     * 是否唯一
     */
    @DbIndexName(name = "是否唯一",order = 4)
    public Boolean unique;
    /**
     * 索引序列
     */
    @DbIndexName(name = "索引序列",order = 5)
    public Integer seqIndex;
    /**
     * 注释
     */
    @DbIndexName(name="注释",order=Integer.MAX_VALUE)
    public String comments;


    /**
     * 获取unique
     *
     * @return unique
     */
    public Boolean getUnique() {
        return unique;
    }

    /**
     * 设置unique
     *
     * @param unique unique
     */
    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    /**
     * 获取seqIndex
     *
     * @return seqIndex
     */
    public Integer getSeqIndex() {
        return seqIndex;
    }

    /**
     * 设置seqIndex
     *
     * @param seqIndex seqIndex
     */
    public void setSeqIndex(Integer seqIndex) {
        this.seqIndex = seqIndex;
    }

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
     * 获取type
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置type
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
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
