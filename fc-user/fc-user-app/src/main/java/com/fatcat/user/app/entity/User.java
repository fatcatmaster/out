package com.fatcat.user.app.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户表
 *
 * @author fatcat
 * @create 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fc_user")
public class User {


    /**
     * 用户ID
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户账号
     */
    @TableField("user_phone")
    private String userPhone;

    /**
     * 用户密码
     */
    @TableField("user_password")
    private String userPassword;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建者UID
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人UID
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 是否已删除[0:否,1:是]
     */
    @TableLogic
    private Boolean isDeleted;


}
