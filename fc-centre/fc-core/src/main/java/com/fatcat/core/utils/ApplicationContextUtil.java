package com.fatcat.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * 也可以使用传统方式通过读取application.xml文件获取spring上下文，但是每次获取都会刷新bean实例，性能消耗高，速度慢
 * 官方提供接口 ApplicationContextAware，只需实现当前接口重写指定方法即可获取到spring上下文，本例采用官方推荐方法
 *
 * @author fatcat
 * @description 容器上下文工具类
 * @create 2021/5/10
 **/
@SpringBootConfiguration
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 通过类型获取指定 bean，如果当前类型对应的bean不唯一会报错
     */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 通过名称获取指定 bean，如果当前名称对应的bean不唯一会报错
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

}
