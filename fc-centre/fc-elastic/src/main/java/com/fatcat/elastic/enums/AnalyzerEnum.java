package com.fatcat.elastic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fatcat
 * @description es 分词器枚举
 * @create 2021/4/19
 **/
@AllArgsConstructor
@Getter
public enum  AnalyzerEnum {
    /**
     * 指定不使用分词
     */
    NO(null),

    /**
     * 标准分词，默认分词器
     */
    STANDARD("standard"),

    /**
     * ik_smart：会做最粗粒度的拆分；已被分出的词语将不会再次被其它词语占有
     */
    IK_SMART("ik_smart"),

    /**
     * ik_max_word ：会将文本做最细粒度的拆分；尽可能多的拆分出词语
     */
    IK_MAX_WORD("ik_max_word")
    ;

    private String type;
}
