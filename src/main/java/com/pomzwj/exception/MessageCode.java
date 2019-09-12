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
    DATABASE_LINK_IS_NULL_ERROR("000001","数据库连接错误，请检查账户密码");

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
