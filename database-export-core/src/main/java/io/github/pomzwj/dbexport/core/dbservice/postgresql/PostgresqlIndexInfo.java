package io.github.pomzwj.dbexport.core.dbservice.postgresql;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

/**
 * postgresql数据库索引信息
 *
 * @author PomZWJ
 */
public class PostgresqlIndexInfo extends DbIndexInfo {
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
}
