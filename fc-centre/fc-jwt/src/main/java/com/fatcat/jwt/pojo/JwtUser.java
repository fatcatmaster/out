package com.fatcat.jwt.pojo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fatcat
 * @description 用于加密的对象信息
 * @create 2021/4/7
 **/
@Data
@Builder
public class JwtUser implements Serializable {

    private static final long serialVersionUID = 108264745216154533L;

    /**
     * 对应加密部分的 Audience
     */
    private String uid;

    /**
     * 用于生成签名的密钥
     */
    private String pwd;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户账号
     */
    private String phone;
}
