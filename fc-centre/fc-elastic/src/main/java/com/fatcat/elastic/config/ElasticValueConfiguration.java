package com.fatcat.elastic.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author fatcat
 * @description es 的参数配置类
 * @create 2021/4/22
 **/
@ConfigurationProperties(prefix = "spring.elastic")
@Data
public class ElasticValueConfiguration {
    /**
     * es节点
     */
    private List<String> uris;
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否开启es连接
     */
    private boolean connectEnabled;
    /**
     * 连接超时时间
     */
    private int connectTimeout;
    /**
     * 会话超时时间
     */
    private int socketTimeout;
    /**
     * 获取连接超时时间
     */
    private int requestTimeout;
    /**
     * 最大连接数
     */
    private int maxConnect;
    /**
     * 最大路由数连接数
     */
    private int maxConnectRoute;
}
