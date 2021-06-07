package com.fatcat.common.constants.enums;

import com.fatcat.common.base.ExceptionBase;
import lombok.AllArgsConstructor;

/**
 * 务异常枚举
 *
 * @author fatcat
 * @description 业务异常枚举
 * @create 2021/4/30
 **/
@AllArgsConstructor
public enum ExceptionEnum implements ExceptionBase {
    /**
     * 响应体的封装
     */
    SUCCESS(200, "请求成功"),
    NOT_FOUND(404, "请求不存在"),
    SERVER_ERROR(500, "服务器异常"),

    REQUEST_FORBIDDEN(1000, "请求不允许"),
    REQUEST_PARSING_ERROR(1001, "请求解析错误"),
    REQUEST_PARAM_ERROR(1002, "请求参数错误"),

    TOKEN_MUST(2000, "令牌不存在"),
    TOKEN_INVALID(2001, "令牌无效"),
    TOKEN_EXPIRED(2002, "令牌失效"),
    TOKEN_ERROR(2003, "令牌错误"),

    MISS_INDEX_ANNOTATION(3000, "缺少[EsIndex]注解"),
    INDEX_IS_EXISTS(3001, "索引已存在"),
    INDEX_CREATE_FAILURE(3002, "创建索引失败"),
    INDEX_UPDATE_FAILURE(3003, "更新索引失败"),
    INDEX_DELETE_FAILURE(3004, "删除索引失败"),
    BATCH_EXECUTE_FAILURE(3005, "批量执行失败"),
    MISS_INDEX_NAME(3006, "未指定索引名称"),
    ;

    /**
     * 异常编码
     */
    private int code;
    /**
     * 异常信息
     */
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
