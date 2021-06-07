package com.fatcat.user.app;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author fatcat
 * @description 用户中心服务
 * @create 2021/4/2
 */
@SpringCloudApplication
@EnableFeignClients("com.fatcat.*.api.feign")
public class FcUserAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcUserAppApplication.class, args);
    }

}
