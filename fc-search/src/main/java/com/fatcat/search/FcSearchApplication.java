package com.fatcat.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author fatcat
 * @description 全局检索服务
 * @create 2021/3/23
 */
@SpringCloudApplication
@EnableFeignClients("com.fatcat.*.api.feign")
public class FcSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcSearchApplication.class, args);
    }

}
