package com.bulain.rabbit;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:spring/applicationContext-rabbit.xml")
public class RabbitInit {

}
