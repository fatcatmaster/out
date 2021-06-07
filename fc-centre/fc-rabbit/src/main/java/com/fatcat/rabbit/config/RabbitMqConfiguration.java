package com.fatcat.rabbit.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;

/**
 * @author fatcat
 * @description rabbit 全局配置
 * @create 2021/4/28
 **/
@SpringBootConfiguration
@EnableConfigurationProperties(RabbitValueConfiguration.class)
@Slf4j
public class RabbitMqConfiguration {

    /**
     * 消息的来源服务，全局设置
     */
    private final static String APP_ID = "fc";
    /**
     * 数据传输类型，json格式
     */
    private final static String CONTENT_TYPE = MessageProperties.CONTENT_TYPE_JSON;
    /**
     * 数据编码格式，utf-8
     */
    private final static String CONTENT_ENCODING = StandardCharsets.UTF_8.name();

    @Autowired
    private RabbitValueConfiguration rabbitValueConfiguration;

    /**
     * 注入全局序列化配置
     * 如果未生效，请在本地自行设置后注入
     * <p>
     * #@see com.fatcat.core.config.JacksonConfiguration#objectMapper()
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        log.info("rabbit connection factory begin to set up...");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(rabbitValueConfiguration.getUri());
        connectionFactory.setVirtualHost(rabbitValueConfiguration.getVirtualHost());
        // 开启发送消息成功回调
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        // 开启推送消息至队列失败回调
        connectionFactory.setPublisherReturns(Boolean.TRUE);
        log.info("rabbit connection factory set successfully!");
        return connectionFactory;
    }

    /**
     * 全局设置消息的回调函数
     * 如果需要单独设置，请将此bean设置成多例模式 @Scope(value = "prototype")
     * 然后单独设置回调函数 setConfirmCallback(), setReturnCallback()
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        // 重写发送消息的方法，保证每次发送消息都传递消息id
        RabbitTemplate rabbitTemplate = new RabbitTemplate() {
            @Override
            public void convertAndSend(@NonNull String exchange, @NonNull String routingKey, @NonNull final Object message) throws AmqpException {
                CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
                this.convertAndSend(exchange, routingKey, message, correlationData);
            }
        };
        rabbitTemplate.setConnectionFactory(connectionFactory());
        // 消息转换器
        MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();
        messagePropertiesConverter.fromMessageProperties(messageProperties(), StandardCharsets.UTF_8.name());
        rabbitTemplate.setMessagePropertiesConverter(messagePropertiesConverter);
        // 设置回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String msgId = ObjectUtil.isNotNull(correlationData) ? correlationData.getId() : "no-id";
            log.warn("消息发送成功：id={}, ack={}, cause={}", msgId, ack, cause);
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.warn("消息被退回：message={}, replyCode={}, replyText={}, exchange={}, routingKey={}",
                    message, replyCode, replyText, exchange, routingKey);
        });
        // 开始强制委托，即回调上述两方法
        rabbitTemplate.setMandatory(Boolean.TRUE);
        // 设置消息转换(生产者)
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    /**
     * 设置rabbit将消息转换成java对象时的处理参数
     */
    @Bean
    public MessageProperties messageProperties() {
        // 个性化参数设置
        MessageProperties messageProperties = new MessageProperties();
        // 设置一个标志，判断消息来源等
        messageProperties.setAppId(APP_ID);
        // 消息的传输格式，json 格式传输
        messageProperties.setContentType(CONTENT_TYPE);
        // 消息的id
        messageProperties.setMessageId(IdUtil.fastUUID());
        // 编码格式
        messageProperties.setContentEncoding(CONTENT_ENCODING);
        // 消息生成时间
        messageProperties.setTimestamp(DateUtil.date());
        return messageProperties;
    }

    /**
     * rabbit消息序列化配置
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * 针对消费者的配置
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 设置链接配置
        factory.setConnectionFactory(connectionFactory());
        // 设置消息转换(消费者)
        factory.setMessageConverter(messageConverter());
        // 设置确认模式为手工确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
