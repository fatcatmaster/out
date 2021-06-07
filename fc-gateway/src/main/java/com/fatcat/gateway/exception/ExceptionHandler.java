package com.fatcat.gateway.exception;

import cn.hutool.json.JSONUtil;
import com.fatcat.common.base.ResultResponse;
import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.core.logger.utils.LogUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 网关异常处理中心
 *
 * @author fatcat
 * @description 网关异常处理中心
 * @create 2021/4/14
 **/
public class ExceptionHandler extends DefaultErrorWebExceptionHandler {
    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public ExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 获取异常属性，会拦截自身以及调用其他服务的异常，统一处理
     * 由于各服务已经处理了自身的异常并正确返回
     * 所以网关应该不再捕获 FatCatException 异常
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable error = super.getError(request);
        LogUtil.error("网关发生异常：", error);
        // 非自定义异常，封装成自定义的异常后返回，默认值
        FatCatException ex;
        if (error instanceof FatCatException) {
            // 如果已经封装成自定义异常，直接返回
            ex = (FatCatException) error;
        } else if (error instanceof NotFoundException) {
            // 请求不存在
            ex = new FatCatException(ExceptionEnum.NOT_FOUND.code(), error.getMessage());
        } else if (error instanceof ResponseStatusException
                && HttpStatus.NOT_FOUND.equals(((ResponseStatusException) error).getStatus())) {
            // 请求不存在
            ex = new FatCatException(ExceptionEnum.NOT_FOUND.code(), error.getMessage());
        } else {
            // 默认异常
            ex = new FatCatException(ExceptionEnum.SERVER_ERROR.code(), error.getMessage());
        }
        ResultResponse response = ResultResponse.failure(ex);
        return JSONUtil.parseObj(JSONUtil.toJsonStr(response));
    }

    /**
     * 指定响应处理方法为 JSON 处理的方法
     *
     * @param errorAttributes 错误属性值
     */
    @Override
    @SuppressWarnings("NullableProblems")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 返回数据给前端的时候调用，即异常处理过后调用
     * 此时数据已经被封装成 {@link com.fatcat.common.base.ResultResponse}
     * code 表示 status; 如果不重写当前方法，默认方法会报错，前端会收不到任何数据
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.get("code");
    }
}
