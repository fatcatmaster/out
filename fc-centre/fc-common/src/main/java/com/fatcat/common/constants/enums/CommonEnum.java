package com.fatcat.common.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局公共常量定义
 *
 * @author fatcat
 * @description 全局公共常量定义
 * @create 2021/4/2
 **/
@AllArgsConstructor
@Getter
public enum CommonEnum {
    /**
     * 公共常量 KEY 值定义
     */
    ACCESS_TOKEN("accessToken", "java web token 键值"),
    HTTP_STATUS("status", "http请求响应的状态编码 键值"),
    HTTP_MESSAGE("message", "http请求响应的异常信息 键值"),
    GATEWAY_CONTEXT("gatewayContext", "网关上下文 键值"),

    /**
     * Jwt 信息键值
     */
    JWT_UID("JWT_UID", "用户id键值"),
    JWT_NAME("JWT_NAME", "用户名称键值"),
    JWT_PHONE("JWT_PHONE", "用户账号键值"),
    JWT_PWD("JWT_PWD", "用户密码键值"),
    ;
    private String key;
    private String desc;
}
