package io.github.pomzwj.dbexport.web.params;

import java.util.List;

public class DbBaseConfigResult {
    private List<DbBaseConfig> columnConfig;
    private List<DbBaseConfig> indexConfig;
    private List<DbBaseConfig> exportTypeConfig;

    public List<DbBaseConfig> getColumnConfig() {
        return columnConfig;
    }

    public void setColumnConfig(List<DbBaseConfig> columnConfig) {
        this.columnConfig = columnConfig;
    }

    public List<DbBaseConfig> getIndexConfig() {
        return indexConfig;
    }

    public void setIndexConfig(List<DbBaseConfig> indexConfig) {
        this.indexConfig = indexConfig;
    }

    public List<DbBaseConfig> getExportTypeConfig() {
        return exportTypeConfig;
    }

    public void setExportTypeConfig(List<DbBaseConfig> exportTypeConfig) {
        this.exportTypeConfig = exportTypeConfig;
    }
}
