package io.github.pomzwj.dbexport.core.dbservice.postgresql;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

public class PostgresqlIndexInfo extends DbIndexInfo {
    @DbIndexName(name="索引名称",order=0)
    public String name;
    @DbIndexName(name="索引类型",order=2)
    public String type;
    @DbIndexName(name = "是否唯一",order = 4)
    public Boolean unique;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
