package com.fatcat.core.logger.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义日志注解，对方法的入参和出参记录日志
 * controller 和 feign 方法默认记录日志，无需再次添加
 *
 * @author fatcat
 * @create 2021/5/10
 * @description 自定义日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logger {
}
