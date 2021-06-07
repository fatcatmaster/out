package com.fatcat.elastic.annotation;

import com.fatcat.elastic.enums.AnalyzerEnum;
import com.fatcat.elastic.enums.FieldEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fatcat
 * @description 用于指定文档字段的类型和分词器
 * @since 2021/4/20
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EsDocField {
    /**
     * 字段类型
     */
    FieldEnum type() default FieldEnum.TEXT;

    /**
     * 分词器
     */
    AnalyzerEnum analyzer() default AnalyzerEnum.STANDARD;
}
