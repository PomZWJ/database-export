package io.github.pomzwj.dbexport.core.dbservice.clickhouse;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

/**
 * clickhouse数据库索引信息
 *
 * @author PomZWJ
 */
public class ClickHouseIndexInfo extends DbIndexInfo {
    /**
     * 索引类型
     */
    @DbIndexName(name="索引类型",order=2)
    public String type;

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
}
