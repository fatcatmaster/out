package com.fatcat.rabbit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fatcat
 * @description rabbit 的配置项
 * @create 2021/4/28
 **/
@ConfigurationProperties(prefix = "spring.rabbit", ignoreInvalidFields = true)
@Data
public class RabbitValueConfiguration {

    /**
     * 主机资源地址（包含通讯方式、用户名、密码、端口）
     */
    private String uri;
    /**
     * rabbit 主机地址
     */
    private String host;
    /**
     * rabbit 服务端口
     */
    private int port;
    /**
     * 登录用户名
     */
    private String name;
    /**
     * 登录用户密码
     */
    private String password;
    /**
     * 当前服务使用的分区地址名
     */
    private String virtualHost;
}
