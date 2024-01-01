package com.bulain.rabbit;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@AllArgsConstructor
public class RabbitWorker {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RabbitClient rabbitClient;

    @RabbitListener(queues = {"demo.topic.1"})
    public void onMessage(Message message) {
        logger.debug("{}", message);
    }

    @SneakyThrows
    @RabbitListener(queues = {"demo.topic.2"})
    public void onMessage(Message message, Channel channel) {
        logger.debug("{}", message);

        MessageProperties messageProperties = message.getMessageProperties();
        channel.basicAck(messageProperties.getDeliveryTag(), false);
    }

    @RabbitListener(queues = {"demo.topic.3"})
    public void handleMessage(String message) {
        logger.debug("{}", message);
    }

    private Random random = new Random();

    @RabbitListener(queues = {"demo.topic.5"})
    public void onMessage(String message) {
        logger.debug("{}", message);

        message = message + "-X";
        int nextInt = random.nextInt();
        if (nextInt % 2 == 0) {
            logger.debug("{}", "send to demo.topic.2");
            rabbitClient.send("demo.2", message, 10000);
        } else {
            logger.debug("{}", "send to demo.topic.4");
            rabbitClient.send("demo.4", message);
        }
    }


}
