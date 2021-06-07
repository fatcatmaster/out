package com.fatcat.user.api.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author fatcat
 * @description 测试 请求响应体
 * @create 2021/4/13
 **/
@Data
public class TestVo implements Serializable {
    private static final long serialVersionUID = -1984958935451556420L;

    @NotNull(message = "id不为空")
    private Long id;

    @NotEmpty(message = "name不为空")
    private String name;
}
