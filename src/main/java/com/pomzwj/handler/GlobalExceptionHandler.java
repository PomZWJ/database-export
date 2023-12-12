package com.pomzwj.handler;

import com.pomzwj.domain.ResponseParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler
    @ResponseBody
    public ResponseParams<String> exceptionHandle(Exception e){ // 处理方法参数的异常类型
        log.error("happend error",e);
        String errorMsg = e.getMessage();
        return new ResponseParams<String>().fail(errorMsg);
    }

}
