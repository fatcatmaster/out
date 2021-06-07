package com.fatcat.core.feign.config;

import com.netflix.hystrix.*;
import feign.Feign;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;
import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;

/**
 * 当前类用来修改 hystrix 的配置，配置文件中针对 hystrix 的配置均是无效
 * proxyBeanMethods = false不使用代理，每次使用新的bean，即多例模式
 * ConditionalOnClass 表示存在参数中的类对象（bean）才会加载当前配置
 *
 * @author fatcat
 * @description 熔断器配置类
 * @create 2021/4/12
 **/
@SpringBootConfiguration(proxyBeanMethods = false)
@EnableConfigurationProperties(HystrixValueConfiguration.class)
@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
public class HystrixConfiguration {

    @Autowired
    private HystrixValueConfiguration hystrixValueConfiguration;

    /**
     * 覆写 Feign.Builder
     */
    @Bean
    @Scope("prototype")
    @ConditionalOnProperty(name = "feign.hystrix.enabled")
    public Feign.Builder feignHystrixBuilder() {
        HystrixFeign.Builder builder = HystrixFeign.builder();
        SetterFactory setterFactory = (target, method) -> {
            String groupKey = target.name();
            String commandKey = Feign.configKey(target.type(), method);
            // HystrixCommand 熔断器相关属性配置
            HystrixCommand.Setter setter = HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
            // HystrixCommandProperties 熔断器相关属性配置
            HystrixCommandProperties.Setter commandSetter = HystrixCommandProperties
                    .Setter()
                    .withExecutionTimeoutInMilliseconds(hystrixValueConfiguration.getTimeOut());
            if (THREAD.equals(hystrixValueConfiguration.getStrategy())) {
                // HystrixThreadPoolProperties 线程池相关配置
                HystrixThreadPoolProperties.Setter threadPoolSetter = HystrixThreadPoolProperties
                        .Setter()
                        .withCoreSize(hystrixValueConfiguration.getThreadCoreSize())
                        .withMaxQueueSize(hystrixValueConfiguration.getMaxRequest());
                commandSetter.withExecutionIsolationStrategy(THREAD);
                setter.andThreadPoolPropertiesDefaults(threadPoolSetter);
            } else {
                // 当前架构默认信号量，错误配置值也默认为信号量
                commandSetter.withExecutionIsolationStrategy(SEMAPHORE)
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(hystrixValueConfiguration.getMaxRequest());
            }
            return setter.andCommandPropertiesDefaults(commandSetter);
        };
        return builder.setterFactory(setterFactory);
    }
}
