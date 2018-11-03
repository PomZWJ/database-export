package com.pomzwj.utils;

/**
 * 类说明:StringUtil工具类
 *
 * @author zhaowenjie<1513041820@qq.com>
 * @date 2018/10/29/0029.
 */
public class StringUtils {
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
