package com.fatcat.user.api.feign;

import com.fatcat.user.api.dto.UserDto;
import com.fatcat.user.api.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author fatcat
 * @description 用户中心对外服务接口
 * @create 2021/4/6
 **/
@FeignClient(contextId = "user", name = "fc-user", path = "/fc-user/api/user")
public interface IUserFeignApi {

    /**
     * 用户登录接口
     *
     * @param userVo 登录信息体
     * @return 用户信息
     */
    @PostMapping("/login")
    UserDto login(@RequestBody UserVo userVo);

    /**
     * 用户注册接口
     *
     * @param userVo 用户信息
     * @return 注册成功的用户
     */
    @PostMapping("/register")
    void register(@RequestBody @Valid UserVo userVo);
}
