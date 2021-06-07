package com.fatcat.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author fatcat
 * @description 服务监控中心
 * @create 2021/3/25
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAdminServer
@EnableWebSecurity
public class FcMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FcMonitorApplication.class, args);
    }

}
