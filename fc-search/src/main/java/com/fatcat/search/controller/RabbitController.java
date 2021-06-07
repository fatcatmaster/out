package com.fatcat.search.controller;

import cn.hutool.json.JSONUtil;
import com.fatcat.rabbit.constants.exchange.DirectExchangeConstants;
import com.fatcat.rabbit.constants.routekey.RouteKeyConstants;
import com.fatcat.search.dto.UserDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息发送 前端控制器
 *
 * @author fatcat
 * @description rabbit 消息发送 前端控制器
 * @create 2021/4/29
 **/
@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public void sendMessage(@RequestBody UserDto user) {
        rabbitTemplate.convertAndSend(DirectExchangeConstants.TEST_DIRECT_EXCHANGE,
                RouteKeyConstants.TEST_DIRECT_ROUTE_KEY, user);
    }
}
