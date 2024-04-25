package io.github.pomzwj.dbexport.web.domain;

import io.github.pomzwj.dbexport.core.exception.MessageCode;
import io.github.pomzwj.dbexport.core.utils.DateUtils;
import io.github.pomzwj.dbexport.core.utils.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Description:Service返回说明
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2019-07-16 15:04
 */
public class ResponseParams<T> implements Serializable {

    /**
     * 返回码
     */
    private String resultCode;

    /**
     * 相应信息
     */
    private String resultMsg;

    /**
     * 操作结束日期
     */
    private String resultDate;

    /**
     * 操作结束的时间
     */
    private Long resultTime;

    /**
     * 返回参数
     */
    private T params;

    /**
     * 返回额外的参数
     */
    private Map<String, Object> extenalParams;

    public String getResultCode() {
        if(StringUtils.isEmpty(resultCode)){
            return MessageCode.SUCCESS.getCode();
        }
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        if(StringUtils.isEmpty(resultMsg)){
            return MessageCode.SUCCESS.getMsg();
        }
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultDate() {
        if(StringUtils.isEmpty(resultDate)){
            resultDate = DateUtils.getCurrentDate();
        }
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public long getResultTime() {
        if(Objects.isNull(resultTime)){
            resultTime = System.currentTimeMillis();
        }
        return resultTime;
    }

    public void setResultTime(Long resultTime) {
        this.resultTime = resultTime;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    public Map<String, Object> getExtenalParams() {
        return extenalParams;
    }

    public void setExtenalParams(Map<String, Object> extenalParams) {
        this.extenalParams = extenalParams;
    }

    public ResponseParams<T> success(MessageCode code,T data){
        this.setResultCode(code.getCode());
        this.setResultMsg(code.getMsg());
        this.setResultTime(System.currentTimeMillis());
        this.setParams(data);
        return this;
    }
    public ResponseParams<T> fail(String msg){
        this.setResultCode(MessageCode.UNKNOWN_ERROR.getCode());
        this.setResultMsg(msg);
        this.setResultTime(System.currentTimeMillis());
        this.setParams(null);
        return this;
    }
}
