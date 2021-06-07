package com.fatcat.common.constants.interfaces;

/**
 * 排序值枚举
 *
 * @author fatcat
 * @description 排序值枚举
 * @create 2021/4/8
 **/
public interface OrderConstants {
    /**
     * 初始化过滤器
     */
    int INIT_FILTER_ORDER = 0;

    /**
     * 路径过滤器
     */
    int PATH_FILTER_ORDER = 10;

    /**
     * 令牌过滤器
     */
    Integer JWT_FILTER_ORDER = 20;
}
