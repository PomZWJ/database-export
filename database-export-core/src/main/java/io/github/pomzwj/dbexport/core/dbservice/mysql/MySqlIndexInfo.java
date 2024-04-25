package io.github.pomzwj.dbexport.core.dbservice.mysql;

import io.github.pomzwj.dbexport.core.anno.DbIndexName;
import io.github.pomzwj.dbexport.core.domain.DbIndexInfo;

public class MySqlIndexInfo extends DbIndexInfo {
    @DbIndexName(name="索引名称",order=0)
    public String name;
    @DbIndexName(name="索引类型",order=2)
    public String type;
    @DbIndexName(name = "是否唯一",order = 4)
    public Boolean unique;
    @DbIndexName(name = "索引序列",order = 5)
    public Integer seqIndex;
    @DbIndexName(name="注释",order=Integer.MAX_VALUE)
    public String comments;


    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Integer getSeqIndex() {
        return seqIndex;
    }

    public void setSeqIndex(Integer seqIndex) {
        this.seqIndex = seqIndex;
    }

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
