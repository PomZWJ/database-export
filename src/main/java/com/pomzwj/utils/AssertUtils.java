package com.pomzwj.utils;

import com.pomzwj.exception.DatabaseExportException;
import com.pomzwj.exception.MessageCode;

import java.util.Objects;

/**
 * 断言
 * @author zhaowj
 * @date 2021-03-04
 */
public class AssertUtils {

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
