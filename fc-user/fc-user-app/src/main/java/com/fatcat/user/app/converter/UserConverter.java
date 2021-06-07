package com.fatcat.user.app.converter;

import com.fatcat.user.api.dto.UserDto;
import com.fatcat.user.api.vo.UserConVo;
import com.fatcat.user.app.entity.User;
import org.mapstruct.Mapper;

/**
* 用户表 转换类
*
* @author fatcat
* @create 2021-05-20
*/
@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConVo entity2Con(User user);

    UserDto entity2Dto(User user);
}