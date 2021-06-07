package com.fatcat.common.base;

import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.exception.FatCatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 全局数据返回体的封装
 *
 * @author fatcat
 * @description 全局数据返回体
 * @create 2021/4/30
 **/
@Data
@NoArgsConstructor
public class ResultResponse implements Serializable {
    private static final long serialVersionUID = 5859098504073316951L;

    /**
     * 响应编码
     */
    private int code;

    /**
     * 数据补充说明
     */
    private String message;

    /**
     * 数据实体
     */
    private Object data;

    /**
     * 当前请求数据是否成功；异常或错误均设置为false
     */
    private Boolean success;

    private ResultResponse(int code, String message, Object data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /**
     * 请求成功时调用
     */
    public static ResultResponse success() {
        return ResultResponse.success(null);
    }

    public static ResultResponse success(Object data) {
        return new ResultResponse(ExceptionEnum.SUCCESS.code(),
                ExceptionEnum.SUCCESS.message(),
                data, Boolean.TRUE);
    }

    /**
     * 自定义异常信息封装
     */
    public static ResultResponse failure(ExceptionBase ex) {
        return new ResultResponse(ex.code(), ex.message(), null, Boolean.FALSE);
    }
    public static ResultResponse failure(FatCatException ex) {
        return new ResultResponse(ex.getCode(), ex.getMessage(), null, Boolean.FALSE);
    }

    /**
     * 系统异常或错误封装
     */
    public static ResultResponse error(Throwable th) {
        return new ResultResponse(ExceptionEnum.SERVER_ERROR.code(),
                ExceptionEnum.SERVER_ERROR.message(),
                th, Boolean.FALSE);
    }
}
