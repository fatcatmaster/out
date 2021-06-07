package com.fatcat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author fatcat
 * @description 服务的网关路由
 * @create 2021/3/23
 */
@SpringCloudApplication
@EnableFeignClients("com.fatcat.*.api.feign")
public class FcGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcGatewayApplication.class, args);
    }

}
