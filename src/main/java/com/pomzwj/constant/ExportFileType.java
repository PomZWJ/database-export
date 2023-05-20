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
    WORD(".docx"),
    EXCEL(".xlsx"),
    MARKDOWN(".md"),
    PDF(".pdf"),
    HTML(".html");
    private String fileSuffixName;

    ExportFileType(String fileSuffixName) {
        this.fileSuffixName = fileSuffixName;
    }

    public String getFileSuffixName() {
        return fileSuffixName;
    }

    public void setFileSuffixName(String fileSuffixName) {
        this.fileSuffixName = fileSuffixName;
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
