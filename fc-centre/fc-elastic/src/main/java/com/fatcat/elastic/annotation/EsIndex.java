package com.fatcat.elastic.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * es 7.X 取消了映射概念，隐藏字段 _type 默认值 “_doc”，对应旧版本的表名
 * 高版本 es 只需要指定索引名即可，对应低版本 es 请自行封装并重写封装方法
 *
 * @author fatcat
 * @description Es 指定索引名称注解
 * @create 2021/4/1
 * @see com.fatcat.elastic.template.ElasticDocTemplate
 * @see com.fatcat.elastic.template.ElasticIndexTemplate
 */
// 注解类、接口、枚举上生效
@Target(ElementType.TYPE)
// 此注解在编译后保留，以便被其他反射机制读取
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EsIndex {

    /**
     * 索引名称
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 索引名称，必填
     */
    @AliasFor("name")
    String value() default "";

}
