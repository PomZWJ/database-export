package io.github.pomzwj.dbexport.core.dbservice.clickhouse;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

public class ClickHouseIndexInfo extends DbIndexInfo {
    @DbIndexName(name="索引类型",order=2)
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
