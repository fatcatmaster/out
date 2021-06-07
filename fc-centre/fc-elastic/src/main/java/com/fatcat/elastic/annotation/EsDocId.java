package com.fatcat.elastic.annotation;

import com.fatcat.elastic.enums.AnalyzerEnum;
import com.fatcat.elastic.enums.FieldEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * es 插入一条记录时如果不手动指定记录id值，引擎会随机一个uuid作为记录的id
 * 手动指定id值方便查询
 * FIELD 作用于类的字段
 *
 * @author fatcat
 * @description 用于指定记录的id注解
 * @create 2021/4/20
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EsDocField
public @interface EsDocId {
    /**
     * 字段类型
     */
    @AliasFor(annotation = EsDocField.class)
    FieldEnum type() default FieldEnum.KEYWORD;

    /**
     * 分词器
     */
    @AliasFor(annotation = EsDocField.class)
    AnalyzerEnum analyzer() default AnalyzerEnum.NO;
}
