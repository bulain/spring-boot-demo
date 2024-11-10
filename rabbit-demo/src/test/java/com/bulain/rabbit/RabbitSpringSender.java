package com.bulain.rabbit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(value = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:spring/applicationContext-resources.xml",
		"classpath:spring/applicationContext-rabbit.xml", "classpath:spring/applicationContext-sender.xml"})
public class RabbitSpringSender {

	@Autowired
	private AmqpTemplate amqpTemplate1;

	@Test
	public void testSend() {
		for (int i = 0; i < 10; i++) {
			amqpTemplate1.convertAndSend("this is a testing " + i);
		}
	}

	@Test
	public void testSendDelay() {
		amqpTemplate1.convertAndSend("demo.2", (Object)"this a delay message",
				message -> MessageBuilder.fromMessage(message).setExpiration(Long.toString(10000L)).build());
	}

}
