package com.bulain.rocket;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

public class RocketConsumer {
    
}

@Slf4j
@Service
@RocketMQMessageListener(topic = "rocketa", selectorExpression = "tag1 | tag2", consumerGroup = "groupa")
class RocketConsumer1a implements RocketMQListener<String> {
    public void onMessage(String message) {
        log.info("received message: {}", message);
    }
}

@Slf4j
@Service
@RocketMQMessageListener(topic = "rocketa", selectorExpression = "tag1 | tag2", consumerGroup = "groupb")
class RocketConsumer1b implements RocketMQListener<String> {
    public void onMessage(String message) {
        log.info("received message: {}", message);
    }
}

@Slf4j
@Service
@RocketMQMessageListener(topic = "rocketb", selectorExpression = "tag3", consumerGroup = "groupc")
class RocketConsumer2 implements RocketMQListener<RocketEvent>{
    public void onMessage(RocketEvent message) {
        log.info("received message: {}", message);
    }
}

@Slf4j
@Service
@RocketMQMessageListener(topic = "rocketc", consumerGroup = "groupd")
class RocketConsumer3 implements RocketMQListener<String>{
    public void onMessage(String message) {
        log.info("received message: {}", message);
    }
}

@Slf4j
@Service
@RocketMQMessageListener(topic = "rocketd", consumerGroup = "groupe")
class RocketConsumer4 implements RocketMQListener<String>{
    public void onMessage(String message) {
        log.info("received message: {}", message);
    }
}
