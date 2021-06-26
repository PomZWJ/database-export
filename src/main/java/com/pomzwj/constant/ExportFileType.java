package com.pomzwj.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 导出文件类型
 * @author PomZWJ
 */
public enum ExportFileType {
    WORD(true),
    EXCEL(false);
    private boolean isEnable;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    ExportFileType(boolean isEnable) {
        this.isEnable = isEnable;
    }
    public static ExportFileType matchType(String value){
        if (StringUtils.isNotEmpty(value)) {
            for (ExportFileType exportFileType: ExportFileType.values()) {
                if (exportFileType.name().equals(value.toUpperCase())) {
                    return exportFileType;
                }
            }
        }
        return null;
    }
}
