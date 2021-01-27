package com.bulain.rocket;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RocketApplication.class)
public class ProducerDemo {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testConvertAndSend() {
        for (int i = 0; i < 100; i++) {
            rocketMQTemplate.convertAndSend("rocketa:tag1", "String Message " + i);
        }
    }

    @Test
    public void testSend() {
        for (int i = 0; i < 100; i++) {
            Message<String> msg = MessageBuilder.withPayload("Payload Message " + i).build();
            rocketMQTemplate.send("rocketa:tag2", msg);
        }
    }

    @Test
    public void testAsyncSend() {
        for (int i = 0; i < 10; i++) {
            rocketMQTemplate.asyncSend("rocketb:tag3", new RocketEvent("E001" + i, new BigDecimal("50")),
                    new SendCallback() {
                        @Override
                        public void onSuccess(SendResult r) {
                            log.info("async onSucess SendResult={}", r);
                        }
                        @Override
                        public void onException(Throwable t) {
                            log.info("async onException Throwable={}", t);
                        }
                    });
        }
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void testSyncSendOrderly() {
        for (int i = 0; i < 100; i++) {
            Message<String> msg = MessageBuilder.withPayload("Orderly Message " + i).build();
            rocketMQTemplate.syncSendOrderly("rocketc:tag4", msg, "hashkey");
        }
    }

    @Test
    public void testSendMessageInTransaction() {
        for (int i = 0; i < 100; i++) {
            Message<String> msg = MessageBuilder.withPayload("Transaction Message " + i).build();
            rocketMQTemplate.sendMessageInTransaction("rocketd:tag5", msg, null);
        }
    }

}
