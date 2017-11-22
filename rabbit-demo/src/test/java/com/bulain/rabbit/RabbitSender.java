package com.bulain.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class RabbitSender {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Test
	public void testSend() {
		amqpTemplate.convertAndSend("queue", "this is a test message");
	}

}
