package com.fatcat.core.feign.config;

import com.netflix.hystrix.HystrixCommandProperties;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author zhouhc
 * @description 自定义熔断器的配置参数
 * @create 2021/5/21
 **/
@SpringBootConfiguration
@ConfigurationProperties(prefix = "spring.hystrix", ignoreInvalidFields = true)
@Data
public class HystrixValueConfiguration {
    /**
     * 熔断策略，自定义默认使用信号量
     */
    private HystrixCommandProperties.ExecutionIsolationStrategy strategy = HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;
    /**
     * 核心线程池的线程大小，策略为线程时生效
     */
    private int threadCoreSize = 100;
    /**
     * 请求处理的队列最大值，线程和信号量处理最大请求量的数
     */
    private int maxRequest = 200;
    /**
     * hystrix 熔断的超时时间，默认 10000，配置值 > feign.connectTimeout + feign.readTimeout
     */
    private int timeOut = 60000;
}
