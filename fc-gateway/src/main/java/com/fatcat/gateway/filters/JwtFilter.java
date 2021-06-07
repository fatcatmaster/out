package com.fatcat.gateway.filters;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fatcat.common.constants.enums.CommonEnum;
import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.constants.enums.RedisKeyEnum;
import com.fatcat.common.constants.interfaces.OrderConstants;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.gateway.context.GatewayContext;
import com.fatcat.gateway.utils.FilterUtil;
import com.fatcat.jwt.pojo.JwtUser;
import com.fatcat.jwt.utils.JwtUtil;
import com.fatcat.user.api.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author fatcat
 * @description 请求拦截器
 * @create 2021/4/1
 **/
@SpringBootConfiguration
public class JwtFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取上下文
        GatewayContext gatewayContext = FilterUtil.getContent(exchange);
        HttpHeaders headers = gatewayContext.getRequestHeaders();
        // 以下处理 jwt token 的验证逻辑
        // 1.获取头部 token
        String token = headers.getFirst(CommonEnum.ACCESS_TOKEN.getKey());
        if (StrUtil.isBlank(token)) {
            throw new FatCatException(ExceptionEnum.TOKEN_MUST);
        }
        // 2.校验 token 有效性
        try {
            JwtUtil.verifyToken(token);
        } catch (JWTVerificationException e) {
            // 捕获异常后, 抛出自定义业务异常
            throw new FatCatException(ExceptionEnum.TOKEN_INVALID);
        }
        try {
            JwtUser jwtUser = JwtUtil.getJwtUser(token);
            // 3.根据 token 获取 redis 中保存的对象值(用户密码)
            UserDto userRedis = (UserDto) redisTemplate.opsForValue()
                    .get(RedisKeyEnum.USER_TOKEN.getKey() + jwtUser.getPhone());
            if (ObjectUtil.isNull(userRedis)) {
                throw new FatCatException(ExceptionEnum.TOKEN_EXPIRED);
            }
        } catch (JWTDecodeException e) {
            throw new FatCatException(ExceptionEnum.TOKEN_EXPIRED);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return OrderConstants.JWT_FILTER_ORDER;
    }
}
