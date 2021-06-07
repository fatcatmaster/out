package com.fatcat.user.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fatcat.mybatis.base.Page;
import com.fatcat.user.api.dto.UserDto;
import com.fatcat.user.api.vo.UserConVo;
import com.fatcat.user.api.vo.UserVo;
import com.fatcat.user.app.entity.User;

/**
 * 用户表 服务类
 *
 * @author fatcat
 * @create 2021-04-06
 */
public interface IUserService extends IService<User> {

    /**
     * 新增一个用户
     *
     * @param userVo 用户信息
     */
    void add(UserVo userVo);

    /**
     * 根据账户和密码登录
     *
     * @param userVo 用户信息
     * @return 用户信息
     */
    UserDto login(UserVo userVo);

    /**
     * 用户注册接口
     *
     * @param userVo 用户信息
     * @return 用户信息
     */
    void register(UserVo userVo);

    /**
     * 转换测试
     *
     * @param userId 用户id
     * @return 用户转换体
     */
    UserConVo converter(Long userId);

    /**
     * 分页请求
     * @param userVo 请求参数
     * @return 分页结果
     */
    Page<User> page(UserVo userVo);
}
