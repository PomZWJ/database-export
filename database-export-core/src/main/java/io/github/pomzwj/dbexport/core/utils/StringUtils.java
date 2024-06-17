package io.github.pomzwj.dbexport.core.utils;

/**
 * 类说明:StringUtil工具类
 *
 * @author PomZWJ
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 得到object值
     * @param value  字符串
     * @return 获取值
     */
    public static String getValue(Object value) {

        if (null == value) {
            return "";
        }
        return value.toString();

    }

    /**
     * 去除字符串中的换行符
     * @param vaa 字符串
     * @return 结果值
     */
    public static String trimLineBreak(String vaa){
        if(isBlank(vaa)){
            return "";
        }else{
            return vaa.replace("\n", "").replace("\r", "");
        }
    }
}
