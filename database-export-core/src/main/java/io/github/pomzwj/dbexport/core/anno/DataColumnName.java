package io.github.pomzwj.dbexport.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库字段列名注解，用于标注字段对应的列名及显示配置
 *
 * @author PomZWJ
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataColumnName {
    /**
     * 获取字段对应的列名
     *
     * @return 列名，默认为空字符串
     */
    String name() default "";

    /**
     * 获取字段的排序顺序
     *
     * @return 排序顺序，默认为0
     */
    int order() default 0;

    /**
     * 判断该字段是否在导出结果中显示
     *
     * @return 是否显示，默认为true
     */
    boolean show() default true;
}
