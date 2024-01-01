package com.bulain.kafka;

import com.bulain.RabbitApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RabbitApplication.class)
class KafkaSendTest {

    @Autowired
    private KafkaClient kafkaClient;

    @Test
    void testProducer() {
        for (int i = 0; i < 10; i++) {
            kafkaClient.send("myTopic", "this is a test message " + i);
        }
    }

    @Test
    void testAck() {
        for (int i = 0; i < 10; i++) {
            kafkaClient.send("ackTopic", "this is a test message " + i);
        }
    }

    @Test
    void testConcurrent() {
        for (int i = 0; i < 100; i++) {
            kafkaClient.send("concurrentTopic", "this is a test message " + i);
        }
    }

}
