package io.github.pomzwj.dbexport.core.exception;

/**
 * 类说明:异常类
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2019/09/12
 */
public class DatabaseExportException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public DatabaseExportException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DatabaseExportException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public DatabaseExportException(MessageCode messageCode){
        super(messageCode.getMsg());
        this.errorCode = messageCode.getCode();
        this.errorMessage = messageCode.getMsg();
    }

    public DatabaseExportException(Throwable e) {
        super(e);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
