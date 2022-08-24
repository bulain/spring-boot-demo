package com.bulain.mybatis.demo.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher implements ApplicationEventPublisherAware {

    private static ApplicationEventPublisher eventPublisher;

    public static ApplicationEventPublisher getEventPublisher(){
        return EventPublisher.eventPublisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        EventPublisher.eventPublisher = applicationEventPublisher;
    }
}
