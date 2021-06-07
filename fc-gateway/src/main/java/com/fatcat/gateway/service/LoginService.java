package com.fatcat.gateway.service;

import cn.hutool.core.util.ObjectUtil;
import com.fatcat.common.constants.enums.RedisKeyEnum;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.core.redis.template.RedisOptTemplate;
import com.fatcat.gateway.exception.ExceptionEnum;
import com.fatcat.jwt.pojo.JwtUser;
import com.fatcat.jwt.utils.JwtUtil;
import com.fatcat.user.api.dto.UserDto;
import com.fatcat.user.api.feign.IUserFeignApi;
import com.fatcat.user.api.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author fatcat
 * @description 登录相关  服务实现
 * @create 2021/4/7
 **/
@Service
public class LoginService {

    /**
     * TOKEN 的有效时间（秒）
     */
    @Value("${fatcat.gateway.expired-time}")
    private Integer expiredTime;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisOptTemplate optTemplate;

    @Autowired
    private IUserFeignApi userFeignApi;

    public UserDto login(UserVo userVo) {
        UserDto userDto = userFeignApi.login(userVo);
        if (ObjectUtil.isNull(userDto)) {
            // 账户或密码错误
            throw new FatCatException(ExceptionEnum.USER_INFO_ERROR);
        }
        // 根据当前用户信息生成 token
        JwtUser jwtUser = JwtUser.builder()
                .uid(String.valueOf(userDto.getUserId()))
                .name(userDto.getUserName())
                .phone(userDto.getUserPhone())
                .pwd(userDto.getUserPassword())
                .build();
        String token = JwtUtil.createToken(jwtUser, expiredTime);
        // 保存 token 至对象中
        userDto.setAccessToken(token);
        // 删除旧 token
        optTemplate.del(RedisKeyEnum.USER_TOKEN.getKey() + userDto.getUserPhone());
        // 将当前 token 和用户信息保存在 redis
        Boolean success = optTemplate.set(RedisKeyEnum.USER_TOKEN.getKey() + userDto.getUserPhone(), userDto, expiredTime);
        if (!success) {
            throw new FatCatException(ExceptionEnum.LOGIN_FAILURE);
        }
        return userDto;
    }

    public String logout() {
        UserDto userDto = new UserDto();
        userDto.setUserPhone("18672872505");
        userDto.setUserName("fatcat");
        userDto.setUserPassword("fatcat");
        redisTemplate.opsForValue().set(RedisKeyEnum.USER_TOKEN.getKey() + userDto.getUserPhone(), userDto, expiredTime, TimeUnit.SECONDS);
        return "success";
    }

    public void register(UserVo userVo) {
//        userFeignApi.register(userVo);
        UserDto userDto = new UserDto();
        userDto.setUserPhone("18672872505");
        userDto.setUserName("fatcat");
        userDto.setUserPassword("fatcat");
        optTemplate.set(RedisKeyEnum.USER_TOKEN.getKey() + userDto.getUserPhone(), userDto, expiredTime);
    }
}
