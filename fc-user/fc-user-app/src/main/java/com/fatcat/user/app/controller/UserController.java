package com.fatcat.user.app.controller;


import com.fatcat.common.base.ResultResponse;
import com.fatcat.user.api.vo.UserVo;
import com.fatcat.user.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户表 前端控制器
 *
 * @author fatcat
 * @create 2021-04-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 新增一个用户
     *
     * @param userVo 用户信息
     */
    @PostMapping
    public ResultResponse add(@RequestBody UserVo userVo) {
        userService.add(userVo);
        return ResultResponse.success();
    }

    /**
     * 分页获取用户信息
     *
     * @param userVo 用户信息
     * @return
     */
    @PostMapping("page")
    public ResultResponse page(@RequestBody UserVo userVo) {
        return ResultResponse.success(userService.page(userVo));
    }

    /**
     * 获取全部用户信息
     */
    @GetMapping("/list")
    public ResultResponse list() {
        return ResultResponse.success(userService.list());
    }

    /**
     * 转换测试
     */
    @GetMapping("converter")
    public ResultResponse converter(@RequestParam("userId") Long userId) {
        return ResultResponse.success(userService.converter(userId));
    }
}
