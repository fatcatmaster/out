package com.fatcat.core.logger.config;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 日志所需 bean 注入
 *
 * @author fatcat
 * @description 日志所需 bean 注入
 * @create 2021/5/10
 **/
@SpringBootConfiguration
public class LoggerConfiguration {
    public final static String LOGGER_DIRECT_EXCHANGE = "fc.logger.direct.exchange";
    public final static String LOGGER_ROUTE_KEY = "fc.logger.route.key";
    public final static String LOGGER_QUEUE = "fc.logger.queue";

    @Bean(LOGGER_QUEUE)
    public Queue loggerQueue() {
        return new Queue(LOGGER_QUEUE);
    }

    @Bean(LOGGER_DIRECT_EXCHANGE)
    public Exchange loggerDirectExchange() {
        return ExchangeBuilder.directExchange(LOGGER_DIRECT_EXCHANGE).build();
    }

    @Bean
    public Binding loggerBinding() {
        return BindingBuilder.bind(loggerQueue()).to(loggerDirectExchange()).with(LOGGER_ROUTE_KEY).noargs();
    }
}
