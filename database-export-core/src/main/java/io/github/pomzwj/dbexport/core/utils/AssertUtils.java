package io.github.pomzwj.dbexport.core.utils;

import io.github.pomzwj.dbexport.core.exception.DatabaseExportException;
import io.github.pomzwj.dbexport.core.exception.MessageCode;

import java.util.Objects;

/**
 * 断言
 * @author PomZWJ
 */
public class AssertUtils {

    /**
     * 断言对象不为null且字符串不为空，否则抛出异常
     *
     * @param var         待校验对象
     * @param messageCode 异常消息码
     */
    public static void isNull(Object var, MessageCode messageCode){
        if(Objects.isNull(var)){
            throw new DatabaseExportException(messageCode.getCode(),messageCode.getMsg());
        }
        if(var instanceof String){
            if(StringUtils.isEmpty((String)var)){
                throw new DatabaseExportException(messageCode.getCode(),messageCode.getMsg());
            }
        }
    }


}
