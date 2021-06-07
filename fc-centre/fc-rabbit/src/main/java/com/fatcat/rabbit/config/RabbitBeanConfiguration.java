package com.fatcat.rabbit.config;

import com.fatcat.rabbit.constants.exchange.DirectExchangeConstants;
import com.fatcat.rabbit.constants.queue.QueueConstants;
import com.fatcat.rabbit.constants.routekey.RouteKeyConstants;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author fatcat
 * @description rabbit 相关bean的注册
 * @create 2021/4/29
 **/
@SpringBootConfiguration
public class RabbitBeanConfiguration {
    @Bean(QueueConstants.TEST_DIRECT_QUEUE)
    public Queue testDirectQueue() {
        return new Queue(QueueConstants.TEST_DIRECT_QUEUE);
    }

    @Bean(DirectExchangeConstants.TEST_DIRECT_EXCHANGE)
    public Exchange testDirectExchange() {
        return ExchangeBuilder.directExchange(DirectExchangeConstants.TEST_DIRECT_EXCHANGE).build();
    }

    @Bean
    public Binding testMessageBinding() {
        return BindingBuilder.bind(testDirectQueue())
                .to(testDirectExchange())
                .with(RouteKeyConstants.TEST_DIRECT_ROUTE_KEY)
                .noargs();
    }
}
