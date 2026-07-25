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
    HTML(".html"),
    EXCEL(".xlsx");
    private String fileSuffixName;

    /**
     * 构造方法
     *
     * @param fileSuffixName 文件后缀名
     */
    ExportFileType(String fileSuffixName) {
        this.fileSuffixName = fileSuffixName;
    }

    /**
     * 获取文件后缀名
     *
     * @return 文件后缀名
     */
    public String getFileSuffixName() {
        return fileSuffixName;
    }

    /**
     * 设置文件后缀名
     *
     * @param fileSuffixName 文件后缀名
     */
    public void setFileSuffixName(String fileSuffixName) {
        this.fileSuffixName = fileSuffixName;
    }

    /**
     * 根据字符串匹配对应的导出文件类型
     *
     * @param value 文件类型字符串
     * @return 匹配到的导出文件类型，未匹配返回null
     */
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
