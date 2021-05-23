package com.bulain.rabbit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(value = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:spring/applicationContext-resources.xml",
		"classpath:spring/applicationContext-rabbit.xml", "classpath:spring/applicationContext-consumer.xml",
		"classpath:spring/applicationContext-sender.xml"})
public class RabbitSpringConsumer {

	@Test
	public void testWorkder() {
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
		}
	}

}
