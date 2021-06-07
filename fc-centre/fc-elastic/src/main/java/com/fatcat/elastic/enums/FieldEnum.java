package com.fatcat.elastic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fatcat
 * @description es 文档字段类型集合
 * @create 2021/4/19
 */
@AllArgsConstructor
@Getter
public enum FieldEnum {

    /**
     * 普通文本类型，一般使用分词器
     */
    TEXT("text"),

    /**
     * 主键类型，会做等值比较，不分词
     */
    KEYWORD("keyword"),

    /**
     * 整型
     */
    INTEGER("integer"),

    /**
     * 浮点型
     */
    DOUBLE("double"),

    /**
     * 布尔型
     */
    BOOLEAN("boolean"),

    /**
     * 日期类型
     */
    DATE("date"),

    /**
     * 单条数据（嵌套对象）
     */
    OBJECT("object"),

    /**
     * 嵌套数组
     */
    NESTED("nested"),
    ;

    private String type;
}
