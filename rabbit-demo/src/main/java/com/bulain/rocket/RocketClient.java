package com.bulain.rocket;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RocketClient implements ApplicationContextAware {

    private ApplicationContext appContext;

    public void send(String routingKey, Object message) {
        RocketMQTemplate template = appContext.getBean(RocketMQTemplate.class);
        template.convertAndSend(routingKey, message);
    }

    public void send(String routingKey, Object message, long delay) {
        RocketMQTemplate template = appContext.getBean(RocketMQTemplate.class);
        template.convertAndSend(routingKey, message);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

}
