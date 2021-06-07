package com.fatcat.common.exception;

import com.fatcat.common.base.ExceptionBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 全局异常封装
 *
 * @author fatcat
 * @description 全局异常封装
 * @create 2021/4/30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FatCatException extends RuntimeException {
    private static final long serialVersionUID = -7056527073546705020L;

    /**
     * 响应编码
     */
    private int code;

    /**
     * 数据补充说明
     */
    private String message;

    public FatCatException(ExceptionBase ex) {
        this.code = ex.code();
        this.message = ex.message();
    }
}
