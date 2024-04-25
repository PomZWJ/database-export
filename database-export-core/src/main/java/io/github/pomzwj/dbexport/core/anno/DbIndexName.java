package io.github.pomzwj.dbexport.core.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author PomZWJ
 * @date 2024-04-27
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbIndexName {
    String name() default "";
    int order() default 0;
    boolean show() default true;
}
