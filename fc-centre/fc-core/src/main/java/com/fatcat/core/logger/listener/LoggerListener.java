package com.fatcat.core.logger.listener;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONUtil;
import com.fatcat.core.logger.config.LoggerConfiguration;
import com.fatcat.core.logger.entity.LoggerEntity;
import com.fatcat.elastic.template.ElasticDocTemplate;
import com.fatcat.elastic.template.ElasticIndexTemplate;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author fatcat
 * @description 日志消息消费记录
 * @create 2021/5/10
 **/
@SpringBootConfiguration
@Slf4j
public class LoggerListener {

    @Autowired
    private ElasticIndexTemplate indexTemplate;

    @Autowired
    private ElasticDocTemplate docTemplate;

    @RabbitListener(queues = LoggerConfiguration.LOGGER_QUEUE)
    public void logger(Message message, Channel channel) throws IOException {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            LoggerEntity logger = JSONUtil.toBean(json, LoggerEntity.class);
            // 先判断所以是否存在，不存在创建
            indexTemplate.createIndex(LoggerEntity.class);
            // 往索引插入日志
            docTemplate.saveOrUpdate(logger);
            // 确认消息，并消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (ConvertException ce) {
            log.warn("日志消息格式不正确", ce);
            // 转换错误的消息消费掉，属于垃圾数据
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } catch (Exception ex) {
            log.error("日志消息同步异常", ex);
            // 重放回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
