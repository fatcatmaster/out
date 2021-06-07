package com.fatcat.user.api.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fatcat
 * @description 用户信息 请求响应体
 * @create 2021/4/6
 **/
@Data
@ToString
public class UserVo implements Serializable {
    private static final long serialVersionUID = 3922699877763753174L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不为空")
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "手机号不为空")
    private String userPhone;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不为空")
    private String userPassword;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者UID
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人UID
     */
    private Long updateUser;

    /**
     * 是否已删除[0:否,1:是]
     */
    private Boolean isDeleted;

    /**
     * 页码
     */
    private int current = 1;

    /**
     * 分页大小
     */
    private int size = 10;
}
