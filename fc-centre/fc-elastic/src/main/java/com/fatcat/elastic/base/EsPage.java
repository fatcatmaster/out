package com.fatcat.elastic.base;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fatcat
 * @description es 数据分页实体封装类
 * @create 2021/5/12
 **/
@Data
public class EsPage<T> implements Serializable {
    private static final long serialVersionUID = 4818672803196769363L;
    /**
     * 用于查询的实体参数设置
     */
    private T search;
    /**
     * 当前页码（从 1 开始）
     */
    private int current = 1;
    /**
     * 每页大小
     */
    private int size = 10;
    /**
     * 结果总数目
     */
    private long total;
    /**
     * 结果详情条目
     */
    private List<T> records;
    /**
     * 排序字段和规则
     */
    private List<EsOrder> orders;
}
