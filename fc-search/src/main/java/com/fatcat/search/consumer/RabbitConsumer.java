package com.fatcat.search.consumer;

import cn.hutool.json.JSONUtil;
import com.fatcat.core.logger.utils.LogUtil;
import com.fatcat.rabbit.constants.queue.QueueConstants;
import com.fatcat.search.dto.UserDto;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringBootConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author fatcat
 * @description rabbit 消息消费者
 * @create 2021/4/29
 **/
@SpringBootConfiguration
public class RabbitConsumer {

    @RabbitListener(queues = QueueConstants.TEST_DIRECT_QUEUE)
    public void testDirectQueue(Message message, Channel channel) throws IOException {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            LogUtil.info("接收消息成功：{}", msg);
            UserDto user = JSONUtil.toBean(msg, UserDto.class);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
        } catch (Exception ex) {
            LogUtil.error("接收消息失败，重新放回队列", ex);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE, Boolean.TRUE);
        }
    }
}
