package com.fatcat.gateway.exception;

import com.fatcat.common.base.ExceptionBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fatcat
 * @description 网关业务异常枚举
 * @create 2021/4/8
 */
@AllArgsConstructor
@Getter
public enum ExceptionEnum implements ExceptionBase {

    /**
     * 网关业务异常枚举
     */
    USER_INFO_ERROR(10000, "用户账号或密码错误"),
    LOGIN_FAILURE(10001, "登录失败"),
    ;

    private int code;
    private String message;

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
