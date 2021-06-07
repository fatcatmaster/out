package com.fatcat.common.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fatcat
 * @description Redis 公共常量定义
 * @create 2021/4/2
 **/
@AllArgsConstructor
@Getter
public enum RedisKeyEnum {
    /**
     * Redis 公共常量定义
     */
    USER_TOKEN("USER_TOKEN:", "用户 token 的键值 key 前缀"),
    ;

    private String key;
    private String desc;

}
