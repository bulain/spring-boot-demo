package com.bulain.rocket;

import com.bulain.evt.DataEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RocketWorker {

    @Slf4j
    @Service
    @RocketMQMessageListener(topic = "rocketa", selectorExpression = "tag1 || tag2", consumerGroup = "groupa")
    static class RocketConsumer1a implements RocketMQListener<String> {
        public void onMessage(String message) {
            log.info("received message: {}", message);
        }
    }

    @Slf4j
    @Service
    @RocketMQMessageListener(topic = "rocketa", selectorExpression = "tag1 || tag2", consumerGroup = "groupb")
    static class RocketConsumer1b implements RocketMQListener<String> {
        public void onMessage(String message) {
            log.info("received message: {}", message);
        }
    }

    @Slf4j
    @Service
    @RocketMQMessageListener(topic = "rocketb", selectorExpression = "tag3", consumerGroup = "groupc")
    static class RocketConsumer2 implements RocketMQListener<DataEvent> {
        public void onMessage(DataEvent message) {
            log.info("received message: {}", message);
        }
    }

    @Slf4j
    @Service
    @RocketMQMessageListener(topic = "rocketc", consumerGroup = "groupd")
    static class RocketConsumer3 implements RocketMQListener<String> {
        public void onMessage(String message) {
            log.info("received message: {}", message);
        }
    }

    @Slf4j
    @Service
    @RocketMQMessageListener(topic = "rocketd", consumerGroup = "groupe")
    static class RocketConsumer4 implements RocketMQListener<String> {
        public void onMessage(String message) {
            log.info("received message: {}", message);
        }
    }

}
