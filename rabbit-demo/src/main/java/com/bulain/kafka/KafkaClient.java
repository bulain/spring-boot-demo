package com.bulain.kafka;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient implements ApplicationContextAware {

    private ApplicationContext appContext;

    public void send(String routingKey, Object message) {
        RoutingKafkaTemplate rabbitTemplate = appContext.getBean(RoutingKafkaTemplate.class);
        rabbitTemplate.send(routingKey, message);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

}
