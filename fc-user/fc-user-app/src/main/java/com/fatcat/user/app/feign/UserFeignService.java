package com.fatcat.user.app.feign;

import com.fatcat.user.api.dto.UserDto;
import com.fatcat.user.api.feign.IUserFeignApi;
import com.fatcat.user.api.vo.UserVo;
import com.fatcat.user.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fatcat
 * @description feign接口的控制器
 * @create 2021/4/8
 **/
@RestController
@RequestMapping("/api/user")
public class UserFeignService implements IUserFeignApi {

    @Autowired
    private IUserService iUserService;

    @Override
    public UserDto login(UserVo userVo) {
        return iUserService.login(userVo);
    }

    @Override
    public void register(UserVo userVo) {
        iUserService.register(userVo);
    }
}
