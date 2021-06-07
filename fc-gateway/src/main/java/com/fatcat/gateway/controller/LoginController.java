package com.fatcat.gateway.controller;

import com.fatcat.common.base.ResultResponse;
import com.fatcat.gateway.service.LoginService;
import com.fatcat.user.api.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关 前端控制器
 *
 * @author fatcat
 * @create 2021/4/7
 **/
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param userVo 登录信息
     */
    @PostMapping("/login")
    public ResultResponse login(@RequestBody UserVo userVo) {
        return ResultResponse.success(loginService.login(userVo));
    }

    /**
     * 用户登出
     */
    @GetMapping("/logout")
    public ResultResponse logout() {
        return ResultResponse.success(loginService.logout());
    }

    /**
     * 用户注册账户
     *
     * @param userVo 用户信息
     */
    @PostMapping("/register")
    public ResultResponse register(@RequestBody UserVo userVo) {
        loginService.register(userVo);
        return ResultResponse.success();
    }
}
