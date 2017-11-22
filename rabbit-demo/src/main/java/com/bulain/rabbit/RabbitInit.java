package com.bulain.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitInit {

	@Bean
    public Queue helloQueue() {
        return new Queue("queue");
    }
	
}
