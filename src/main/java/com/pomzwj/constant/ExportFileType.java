package com.pomzwj.constant;

import com.deepoove.poi.XWPFTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导出文件类型
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 */
public enum ExportFileType {
    WORD(true),
    EXCEL(true),
    MARKDOWN(true);
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
