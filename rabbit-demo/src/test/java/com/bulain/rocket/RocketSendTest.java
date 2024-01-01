package com.bulain.rocket;

import com.bulain.RabbitApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@Slf4j
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RabbitApplication.class)
class RocketSendTest {
    @Resource
    private RocketClient rocketClient;

    @Test
    void testConvertAndSend() {
        for (int i = 0; i < 100; i++) {
            rocketClient.send("rocketa:tag1", "String Message " + i);
        }
    }

    @Test
    void testSend() {
        for (int i = 0; i < 100; i++) {
            Message<String> msg = MessageBuilder.withPayload("Payload Message " + i).build();
            rocketClient.send("rocketa:tag2", msg);
        }
    }

}
