package com.fatcat.user.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouhc
 * @description
 * @create 2021/5/20
 **/
@Data
public class UserConVo implements Serializable {

    private static final long serialVersionUID = 7299964894812211377L;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private Long userName;

    /**
     * 用户账号
     */
    private String userPhone;
}
