package com.pomzwj.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanContext implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取上下文
     *
     * @return 上下文对象
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 根据beanName获取bean
     *
     * @param beanName bean名称
     * @return bean对象
     */
    public Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * 根据beanName和类型获取bean
     *
     * @param beanName bean名称
     * @param clazz    bean类型
     * @param <T>      bean类型
     * @return bean对象
     */
    public <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    /**
     * 根据类型获取bean
     *
     * @param clazz bean类型
     * @param <T>   bean类型
     * @return bean对象
     */
    public <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
