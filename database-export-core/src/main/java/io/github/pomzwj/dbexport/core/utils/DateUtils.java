package io.github.pomzwj.dbexport.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 类说明:日期工具类
 *
 * @author PomZWJ
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
}
