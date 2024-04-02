package com.seciii.prism030.core.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RabbitProducerController {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    /**
     * 生产者向hello消息队列发送消息
     * @param msg
     * @return
     */
    @PostMapping("/produce")
    public String produce(@RequestBody String msg) {
        rabbitTemplate.convertAndSend("news_queue", msg);
        return "消息发送成功";
    }
}
