package io.github.pomzwj.dbexport.core.constant;

import java.io.File;

public final class DataBaseConfigConstant {

    //默认系统缓存文件夹
    public final static String SYSTEM_FILE_DIR = ".databaseExport";

    //word模版文件
    public final static String IMPORT_TEMPLATE = "docx" + File.separator + "import.docx";
    public final static String SUB_MODEL_TEMPLATE = "docx" + File.separator + "sub_model.docx";

    //html模板文件
    public final static String HTML_TEMPLATE = "docx" + File.separator + "html_template.html";

    //pdf字体库
    public final static String CALLIGRAPHIC_TEMPLATE = "ttcf" + File.separator + "simsun.ttc";
}
