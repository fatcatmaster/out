package com.fatcat.elastic.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fatcat
 * @description es 指定排序字段
 * @create 2021/5/13
 **/
@Data
public class EsOrder implements Serializable {
    private static final long serialVersionUID = -8662841283448869349L;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 默认升序排序
     */
    private Boolean asc = Boolean.TRUE;
}
