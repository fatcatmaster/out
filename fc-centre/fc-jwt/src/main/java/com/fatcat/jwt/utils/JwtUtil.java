package com.fatcat.jwt.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fatcat.common.constants.enums.CommonEnum;
import com.fatcat.jwt.pojo.JwtUser;

import java.util.Date;

/**
 * @author fatcat
 * @description JWT 工具类
 * @create 2021/4/1
 **/
public class JwtUtil {

    /**
     * 密钥
     */
    private final static String SECRET = "fatcat-jwt-secret";

    /**
     * @param user 用户签发的对象
     * @param time 有效时长(默认单位:秒)
     * @return 生成的 token
     */
    public static String createToken(JwtUser user, int time) {
        return createToken(user, time, DateField.SECOND);
    }

    /**
     * @param user 用户签发的对象
     * @param time 有效时长
     * @return 生成的 token
     */
    public static String createToken(JwtUser user, int time, DateField unit) {
        // 当前时间
        Date now = DateUtil.date();
        // 过期时间
        Date expiresDate = DateUtil.offset(now, unit, time);
        // 创建 token
        return JWT.create()
                // JWT的唯一识别码, 设置成用户id
                .withJWTId(user.getUid())
                // 自定义的声明值
                .withClaim(CommonEnum.JWT_UID.getKey(), user.getUid())
                .withClaim(CommonEnum.JWT_NAME.getKey(), user.getName())
                .withClaim(CommonEnum.JWT_PHONE.getKey(), user.getPhone())
                .withClaim(CommonEnum.JWT_PWD.getKey(), user.getPwd())
                // 生效时间
                .withIssuedAt(now)
                // 过期时间
                .withExpiresAt(expiresDate)
                // 指定签名算法, 密钥指定为用户密码
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 检验 token 合法性
     *
     * @param token 当前token
     */
    public static void verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        verifier.verify(token);
    }

    /**
     * 获取签发对象，签发对象就是用户的 ID
     *
     * @param token 用户token
     */
    public static String getUserId(String token) throws JWTDecodeException {
        return JWT.decode(token).getId();
    }

    /**
     * 根据token获取用户信息
     *
     * @param token token值
     * @return JwtUser对象
     */
    public static JwtUser getJwtUser(String token) throws JWTDecodeException {
        DecodedJWT jwt = JWT.decode(token);
        return JwtUser.builder()
                .uid(jwt.getClaim(CommonEnum.JWT_UID.getKey()).asString())
                .name(jwt.getClaim(CommonEnum.JWT_NAME.getKey()).asString())
                .phone(jwt.getClaim(CommonEnum.JWT_PHONE.getKey()).asString())
                .pwd(jwt.getClaim(CommonEnum.JWT_PWD.getKey()).asString())
                .build();
    }
}
