package com.bulain.rabbit;

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
	private SimpleMessageListenerContainer topicQueueContainer;
	@Autowired
	private SimpleMessageListenerContainer fanoutQueueContainer;

	@Test
	public void testStartTopic() {
		topicQueueContainer.start();
	}

	@Test
	public void testStartFanout() {
		fanoutQueueContainer.start();
	}

}
