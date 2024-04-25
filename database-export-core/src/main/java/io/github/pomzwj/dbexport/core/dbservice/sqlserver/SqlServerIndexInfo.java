package io.github.pomzwj.dbexport.core.dbservice.sqlserver;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

public class SqlServerIndexInfo extends DbIndexInfo {
    @DbIndexName(name = "索引名称", order = 0)
    public String name;
    @DbIndexName(name = "索引类型", order = 2)
    public String indexType;
    @DbIndexName(name = "索引序列", order = 3)
    public Integer indexId;
    @DbIndexName(name = "索引升序", order = 4)
    public Boolean descending;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }
}
