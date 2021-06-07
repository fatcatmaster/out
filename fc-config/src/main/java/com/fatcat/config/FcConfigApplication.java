package com.fatcat.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author fatcat
 * @description 读取远程配置的服务中心
 * @create 2021/3/25
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class FcConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcConfigApplication.class, args);
    }

}
