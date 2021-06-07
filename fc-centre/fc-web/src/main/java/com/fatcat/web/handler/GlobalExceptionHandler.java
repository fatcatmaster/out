package com.fatcat.web.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fatcat.common.base.ResultResponse;
import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.core.logger.utils.LogUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常结果处理器
 *
 * @author fatcat
 * @description 全局异常处理器
 * @create 2021/4/12
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常，业务异常属于正确逻辑，返回状态码设置为 200，其他均为 500
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(FatCatException.class)
    public ResultResponse handleFatCatException(FatCatException ex, HttpServletRequest request) {
        LogUtil.error("业务异常：", ex);
        return ResultResponse.failure(ex);
    }

    /**
     * 处理 Get 请求中 使用 @Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BindException.class)
    public ResultResponse handleBindException(BindException bind) {
        LogUtil.error("GET 请求参数验证异常：", bind);
        String message = bind.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(StrUtil.COMMA));
        FatCatException ex = new FatCatException(ExceptionEnum.REQUEST_PARAM_ERROR.code(), message);
        return ResultResponse.failure(ex);
    }

    /**
     * 处理请求参数 @RequestBody 上 @Valid 失败后抛出的异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException method) {
        LogUtil.error("JSON 数据验证异常：", method);
        String message = method.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(StrUtil.COMMA));
        FatCatException ex = new FatCatException(ExceptionEnum.REQUEST_PARAM_ERROR.code(), message);
        return ResultResponse.failure(ex);
    }

    /**
     * Get请求中 @RequestParam 必填项缺失异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultResponse handleConstraintViolationException(MissingServletRequestParameterException miss) {
        LogUtil.error("GET 请求参数必填项缺失：", miss);
        String message = miss.getMessage();
        FatCatException ex = new FatCatException(ExceptionEnum.REQUEST_PARAM_ERROR.code(), message);
        return ResultResponse.failure(ex);
    }

    /**
     * 处理请求参数格式错误 @RequestParam 上 validate 失败后抛出的异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultResponse handleConstraintViolationException(ConstraintViolationException con) {
        LogUtil.error("GET 请求参数格式错误：", con);
        String message = con.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(StrUtil.COMMA));
        FatCatException ex = new FatCatException(ExceptionEnum.REQUEST_PARAM_ERROR.code(), message);
        return ResultResponse.failure(ex);
    }

    /**
     * 拦截请求不存在异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultResponse handleNotFound(NoHandlerFoundException not) {
        LogUtil.error("GET 请求参数格式错误：", not);
        FatCatException ex = new FatCatException(ExceptionEnum.NOT_FOUND.code(), not.getMessage());
        return ResultResponse.failure(ex);
    }

    /**
     * 拦截其他异常，@ResponseStatus 用于修改返回体的状态码，统一返回200
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResultResponse handleException(Exception th, HttpServletRequest request) {
        LogUtil.error("系统发生错误：" + JSONUtil.toJsonStr(request), th);
        FatCatException ex = new FatCatException(ExceptionEnum.SERVER_ERROR.code(), th.getMessage());
        return ResultResponse.failure(ex);
    }
}
