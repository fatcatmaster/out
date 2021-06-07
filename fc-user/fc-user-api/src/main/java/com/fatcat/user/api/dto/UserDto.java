package com.fatcat.user.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fatcat
 * @description 用户信息 数据传输对象
 * @create 2021/4/6
 **/
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 3922699877763753174L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userPhone;

    /**
     * 用户密码
     */
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
     * 用户 token
     */
    private String accessToken;
}
