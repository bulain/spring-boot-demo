package com.bulain.rabbit;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class RabbitConsumer {

	@Autowired
	private SimpleMessageListenerContainer topicQueueContainer1;
	@Autowired
	private SimpleMessageListenerContainer topicQueueContainer2;
	@Autowired
	private SimpleMessageListenerContainer fanoutQueueContainer;

	@SuppressWarnings("static-access")
	@AfterClass
	public static void tearDownClass(){
		try {
			Thread.currentThread().sleep(2000L);
		} catch (InterruptedException e) {
		}
	}
	
	@Test
	public void testStartTopic1() {
		topicQueueContainer1.start();
	}
	
	@Test
	public void testStartTopic2() {
		topicQueueContainer2.start();
	}

	@Test
	public void testStartFanout() {
		fanoutQueueContainer.start();
	}

}
