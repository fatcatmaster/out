package com.fatcat.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author fatcat
 * @description 所有服务的注册中心
 * @create 2021/3/23
 */
@SpringBootApplication
@EnableEurekaServer
public class FcEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcEurekaApplication.class, args);
    }

}
