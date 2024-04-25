package io.github.pomzwj.dbexport.core.utils;

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

    /**
     * 去除字符串中的换行符
     * @param vaa
     * @return
     */
    public static String trimLineBreak(String vaa){
        if(isBlank(vaa)){
            return "";
        }else{
            return vaa.replace("\n", "").replace("\r", "");
        }
    }
}
