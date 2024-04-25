package io.github.pomzwj.dbexport.core.dbservice.db2;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

public class Db2IndexInfo extends DbIndexInfo {
    @DbIndexName(name="索引名称",order=0)
    public String name;
    @DbIndexName(name="索引类型",order=2)
    public String type;
    @DbIndexName(name="注释",order=Integer.MAX_VALUE)
    public String comments;

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
