package io.github.pomzwj.dbexport.core.exception;

/**
 * 类说明:异常类
 *
 * @author PomZWJ
 */
public class DatabaseExportException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    /**
     * 构造方法
     *
     * @param errorCode    错误代码
     * @param errorMessage 错误信息
     */
    public DatabaseExportException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 构造方法
     *
     * @param errorMessage 错误信息
     */
    public DatabaseExportException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    /**
     * 构造方法
     *
     * @param messageCode 消息代码
     */
    public DatabaseExportException(MessageCode messageCode){
        super(messageCode.getMsg());
        this.errorCode = messageCode.getCode();
        this.errorMessage = messageCode.getMsg();
    }

    /**
     * 构造方法
     *
     * @param e 异常对象
     */
    public DatabaseExportException(Throwable e) {
        super(e);
    }

    /**
     * 获取错误代码
     *
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误代码
     *
     * @param errorCode 错误代码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误信息
     *
     * @param errorMessage 错误信息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
