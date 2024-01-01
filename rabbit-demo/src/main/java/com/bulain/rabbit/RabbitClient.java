package com.bulain.rabbit;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RabbitClient implements ApplicationContextAware {

    private ApplicationContext appContext;

    public void send(String routingKey, Object message) {
        RabbitTemplate rabbitTemplate = appContext.getBean(RabbitTemplate.class);
        rabbitTemplate.convertAndSend(routingKey, message);
    }

    public void send(String routingKey, Object message, long delay) {
        RabbitTemplate rabbitTemplate = appContext.getBean(RabbitTemplate.class);
        rabbitTemplate.convertAndSend(routingKey, message,
                p -> MessageBuilder.fromMessage(p).setExpiration(Long.toString(delay)).build());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

}
