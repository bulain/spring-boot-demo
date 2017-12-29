package com.bulain.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(value = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:spring/applicationContext-resources.xml",
		"classpath:spring/applicationContext-rabbit.xml", "classpath:spring/applicationContext-sender.xml"})
public class RabbitSpringSender {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Test
	public void testSend() {
		amqpTemplate.convertAndSend("this is a testing");
	}

}
