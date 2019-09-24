package com.pomzwj.exception;

/**
 * 类说明:信息代码
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2019/09/12
 */
public enum MessageCode {
    SUCCESS("000000","处理成功"),
    UNKNOWN_ERROR("999999","未知错误"),
    DATABASE_LINK_IS_NULL_ERROR("000001","数据库连接错误，请检查账户密码"),
    DATABASE_NOT_EXISTS_ERROR("000002","数据库不存在"),
    DATABASE_IP_IS_NULL_ERROR("000003","数据库地址不能为空"),
    DATABASE_PORT_IS_NULL_ERROR("000004","数据库端口不能为空"),
    DATABASE_NAME_IS_NULL_ERROR("000005","数据库库名/表空间不能为空"),
    DATABASE_USER_IS_NULL_ERROR("000006","数据库用户ID不能为空"),
    DATABASE_PASSWORD_IS_NULL_ERROR("000007","数据库密码不能为空"),
    DATABASE_DRIVE_IS_NULL_ERROR("000008","数据库驱动不存在"),
    FILE_PATH_IS_NULL_ERROR("000009","生成的文档位置不能为空"),
    FILE_DIRECT_IS_NOT_EXISTS_ERROR("000010","自定义目录在磁盘上不存在"),
    FILE_IS_NOT_DIRECT_ERROR("000011","自定义的目录不是一个文件夹");
    String code;
    String msg;
    MessageCode(String code,String msg){
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
