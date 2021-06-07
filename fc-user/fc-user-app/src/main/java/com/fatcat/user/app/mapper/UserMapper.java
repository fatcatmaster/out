package com.fatcat.user.app.mapper;

import com.fatcat.mybatis.base.Page;
import com.fatcat.user.app.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 *
 * @author fatcat
 * @create 2021-04-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Page<User> selectPage(Page<User> page);
}
