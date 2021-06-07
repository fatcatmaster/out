package com.fatcat.search.vo;

import com.fatcat.elastic.base.EsOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fatcat
 * @description 用户分页请求
 * @create 2021/5/13
 **/
@Data
public class UserPageVo implements Serializable {
    private static final long serialVersionUID = -5295985636264762809L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 当前页码（从 1 开始）
     */
    private int current;
    /**
     * 每页大小
     */
    private int size;
    /**
     * 用于查询es数据的排序
     */
    private List<EsOrder> orders;
}
