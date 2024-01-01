package com.bulain.rabbit;

import com.bulain.RabbitApplication;
import com.bulain.evt.DataEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RabbitApplication.class)
class RabbitSendTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RabbitClient rabbitClient;

    @Test
    void testSendByRoutingKeySp() {
        for (int i = 0; i < 100; i++) {
            rabbitClient.send("topic.1", "this is a topic.1 message " + i);
        }
    }

    @Test
    void testSendByRoutingKey() {

        rabbitClient.send("direct", "this is a direct.1 message");
        rabbitClient.send("topic.1", "this is a topic.1 message");
        rabbitClient.send("topic.2", "this is a topic.2 message");
        rabbitClient.send("fanout", "this is a fanout.1 message");
        rabbitClient.send("fanout", "this is a fanout.2 message");

    }

    @Test
    void testSendByQueue() {

        rabbitClient.send("bulain.direct.queue", "this is a direct.2 message");
        rabbitClient.send("bulain.topic.queue1", "this is a topic.3 message");
        rabbitClient.send("bulain.topic.queue2", "this is a topic.4 message");
        rabbitClient.send("bulain.fanout.queue1", "this is a fanout.3 message");
        rabbitClient.send("bulain.fanout.queue2", "this is a fanout.4 message");

    }

    @Test
    void testSendObject() {
        DataEvent data = new DataEvent();
        data.setId("1");
        data.setName("test");
        data.setDescr("descr");
        rabbitClient.send("bulain.object.queue", data);
    }

}
