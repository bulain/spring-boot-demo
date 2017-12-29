package com.bulain.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
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
