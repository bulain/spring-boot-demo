package com.bulain.rocket;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(topic = "rocket", selectorExpression = "tagb", consumerGroup = "groupb")
public class RocketConsumer2 implements RocketMQListener<RocketEvent>{
    public void onMessage(RocketEvent message) {
        log.info("received message: {}", message);
    }
}
