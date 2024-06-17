package io.github.pomzwj.dbexport.core.type;

import org.apache.commons.lang3.StringUtils;

/**
 * 导出文件类型
 *
 * @author PomZWJ
 */
public enum ExportFileType {
    WORD(".docx"),
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

    public static ExportFileType matchType(String value) {
        if (StringUtils.isNotEmpty(value)) {
            for (ExportFileType exportFileType : ExportFileType.values()) {
                if (exportFileType.name().equals(value.toUpperCase())) {
                    return exportFileType;
                }
            }
        }
        return null;
    }
}
