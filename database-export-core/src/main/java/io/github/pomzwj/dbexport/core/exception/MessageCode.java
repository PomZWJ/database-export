package io.github.pomzwj.dbexport.core.exception;

/**
 * 类说明:信息代码
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2019/09/12
 */
public enum MessageCode {
    SUCCESS("000000","success"),
    UNKNOWN_ERROR("999999","unknow error"),
    DATABASE_LINK_IS_NULL_ERROR("000001","Database connection error, please check account password"),
    DATABASE_NOT_EXISTS_ERROR("000002","Database does not exist"),
    DATABASE_IP_IS_NULL_ERROR("000003","Database URL cannot be empty"),
    DATABASE_PORT_IS_NULL_ERROR("000004","Database port cannot be empty"),
    DATABASE_NAME_IS_NULL_ERROR("000005","Database library name/tablespace cannot be empty"),
    DATABASE_USER_IS_NULL_ERROR("000006","Database user ID cannot be null"),
    DATABASE_PASSWORD_IS_NULL_ERROR("000007","Database password cannot be empty"),
    DATABASE_DRIVE_IS_NULL_ERROR("000008","Database driver does not exist"),
    FILE_PATH_IS_NULL_ERROR("000009","The location of the generated document cannot be empty"),
    FILE_DIRECT_IS_NOT_EXISTS_ERROR("000010","Custom directory does not exist on disk"),
    FILE_IS_NOT_DIRECT_ERROR("000011","A customized directory is not a folder"),
    DATABASE_KIND_IS_NULL_ERROR("000012","DataBase Type is null"),
    DATABASE_KIND_IS_NOT_MATCH_ERROR("000013","Database type not supported"),
    EXPORT_FILE_TYPE_IS_NOT_MATCH_ERROR("000014","Export file type type not supported"),
    EXPORT_FILE_TYPE_IS_NOT_DEVELOP_ERROR("000015","The export file type type has not been developed yet."),
    DATABASE_DB_FILE_IS_NULL_ERROR("000016","The database file address cannot be empty"),
    DATABASE_SCHEMA_IS_NULL_ERROR("000017","Database schema cannot be empty"),
    EXPORT_FILE_TYPE_IS_NULL_ERROR("000018","Export file type type is null"),
    DATABASE_SOURCE_IS_NULL_ERROR("000019","dataSource is null"),
    DATABASE_TABLE_NOT_ANY("000020","this database not find any table"),
    DATABASE_SOURCE_UNRECOGNIZABLE("000021","this database is unrecognizable"),
    DATABASE_CUSTOMIZABLE_COLUMN_ERROR("000022","showColumnList of config, field is not exist"),
    DATABASE_CUSTOMIZABLE_INDEX_ERROR("000023","showIndexList of config, field is not exist"),
    OUTPUT_DIR_IS_NULL_ERROR("000024","no output folder configured"),
    OUTPUT_FILE_FORMAT_IS_NULL_ERROR("000025","no output file format is configured");
    String code;
    String msg;
    MessageCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }}
