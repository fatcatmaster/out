package com.fatcat.search.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fatcat
 * @description 用户数据传输实体
 * @create 2021/4/29
 **/
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = -6245191647323814281L;

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
}
