package io.github.pomzwj.dbexport.core.utils;

import io.github.pomzwj.dbexport.core.exception.DatabaseExportException;
import io.github.pomzwj.dbexport.core.exception.MessageCode;

import java.util.Objects;

/**
 * 断言
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
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
