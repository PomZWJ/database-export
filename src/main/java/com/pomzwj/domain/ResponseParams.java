package com.pomzwj.domain;

import com.pomzwj.exception.MessageCode;
import com.pomzwj.utils.DateUtils;
import com.pomzwj.utils.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Description:Service返回说明
 *
 * @author zhaowenjie<email>1513041820@qq.com</email>
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
    private String resultTime;

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

    public String getResultTime() {
        if(StringUtils.isEmpty(resultTime)){
            resultTime = DateUtils.getCurrentTime();
        }
        return resultTime;
    }

    public void setResultTime(String resultTime) {
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
}
