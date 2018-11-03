package com.pomzwj.domain;

import com.deepoove.poi.data.RowRenderData;

import java.io.Serializable;
import java.util.List;

/**
 * 类说明:Temp缓冲数据
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
public class TempData implements Serializable {
    private String tableName;
    private String tableComment;
    private List<RowRenderData> data;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<RowRenderData> getData() {
        return data;
    }

    public void setData(List<RowRenderData> data) {
        this.data = data;
    }
}
