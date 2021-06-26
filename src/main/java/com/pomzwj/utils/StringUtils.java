package com.pomzwj.utils;

/**
 * 类说明:StringUtil工具类
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2018/10/29/0029.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 得到object值
     * @param value
     * @return
     */
    public static String getValue(Object value) {

        if (null == value) {
            return "";
        }
        return value.toString();

    }
}
