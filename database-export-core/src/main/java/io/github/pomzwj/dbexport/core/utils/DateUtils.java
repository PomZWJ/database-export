package io.github.pomzwj.dbexport.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 类说明:日期工具类
 *
 * @author PomZWJ
 * @email 1513041820@qq.com
 * @github https://github.com/PomZWJ
 * @date 2019/09/12
 */
public class DateUtils {
    /**
     * 获取当前日期,需要定义格式
     * @return 当前时间字符串 yyyyMMdd
     */
    public static String getCurrentDate(String format) {
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat(format);
        String dateChar = sim.format(date);
        return dateChar;
    }

    /**
     * 获取当前日期,不需要定义格式，默认是 yyyyMMdd
     * @return 当前时间字符串 yyyyMMdd
     */
    public static String getCurrentDate() {
        return DateUtils.getCurrentDate("yyyyMMdd");
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间字符串 HHmmss
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("HHmmss");
        String dateChar = sim.format(date);
        return dateChar;
    }
}
