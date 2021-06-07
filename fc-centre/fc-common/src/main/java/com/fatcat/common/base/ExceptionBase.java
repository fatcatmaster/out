package com.fatcat.common.base;

/**
 * 全局异常实现接口
 *
 * @author fatcat
 * @create 2021/4/30
 * @description 全局异常接口
 */
public interface ExceptionBase {

    /**
     * 返回自定义异常的编码
     *
     * @return 编码
     */
    int code();

    /**
     * 返回自定义异常的消息
     *
     * @return 消息
     */
    String message();
}
