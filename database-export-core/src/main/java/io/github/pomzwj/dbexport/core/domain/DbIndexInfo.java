package io.github.pomzwj.dbexport.core.domain;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;

public class DbIndexInfo {
    @DbIndexName(name="索引字段",order=1)
    public String fields;

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
