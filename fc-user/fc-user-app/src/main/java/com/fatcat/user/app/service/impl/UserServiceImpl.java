package com.fatcat.user.app.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatcat.mybatis.base.Page;
import com.fatcat.user.api.dto.UserDto;
import com.fatcat.user.api.vo.UserConVo;
import com.fatcat.user.api.vo.UserVo;
import com.fatcat.user.app.converter.UserConverter;
import com.fatcat.user.app.entity.User;
import com.fatcat.user.app.mapper.UserMapper;
import com.fatcat.user.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户表 服务实现类
 *
 * @author fatcat
 * @create 2021-04-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void add(UserVo userVo) {
        User user = new User();
        user.setCreateTime(LocalDateTimeUtil.now());
        user.setUpdateTime(LocalDateTimeUtil.now());
        user.setCreateUser(0L);
        user.setUpdateUser(0L);
        user.setIsDeleted(Boolean.FALSE);
        user.setUserPhone(userVo.getUserPhone());
        user.setUserName(userVo.getUserName());
        user.setUserPassword(SecureUtil.md5(userVo.getUserPassword()));
        save(user);
    }

    @Override
    public UserDto login(UserVo userVo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserPassword, SecureUtil.md5(userVo.getUserPassword()))
                .and(e -> e.eq(User::getUserName, userVo.getUserName())
                        .or().eq(User::getUserPhone, userVo.getUserPhone()))
                .eq(User::getIsDeleted, Boolean.FALSE);
        User user = getOne(queryWrapper);
        if (ObjectUtil.isNull(user)) {
            return null;
        }
        return userConverter.entity2Dto(user);
    }

    @Override
    public void register(UserVo userVo) {
    }

    @Override
    public UserConVo converter(Long userId) {
        User user = getById(userId);
        UserConVo con = userConverter.entity2Con(user);
        System.out.println(JSONUtil.toJsonStr(con));
        return con;
    }

    @Override
    public Page<User> page(UserVo userVo) {
        Page<User> page = new Page<>();
        page.setSize(userVo.getSize()).setCurrent(userVo.getCurrent());
//        return page(page);
        return userMapper.selectPage(page);
    }
}
