package com.fatcat.core.logger.thread;

import com.fatcat.core.logger.config.LoggerConfiguration;
import com.fatcat.core.logger.entity.LoggerEntity;
import lombok.Getter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author fatcat
 * @description 处理日志消息发送的线程
 * @create 2021/5/12
 **/
public class LogSendThread implements Runnable {

    private RabbitTemplate rabbitTemplate;

    private LoggerEntity logger;

    public LogSendThread(RabbitTemplate rabbitTemplate, LoggerEntity logger) {
        this.rabbitTemplate = rabbitTemplate;
        this.logger = logger;
    }

    @Override
    public void run() {
        rabbitTemplate.convertAndSend(LoggerConfiguration.LOGGER_DIRECT_EXCHANGE, LoggerConfiguration.LOGGER_ROUTE_KEY, logger);
    }
}
