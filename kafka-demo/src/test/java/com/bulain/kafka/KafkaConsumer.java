package com.bulain.kafka;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = KafkaApplication.class)
public class KafkaConsumer {

	@Autowired
	private MessageListenerContainer messageListenerContainer;
	@Autowired
	private MessageListenerContainer ackMessageListenerContainer;
	@Autowired
	private MessageListenerContainer concurrentMessageListenerContainer;

	@AfterAll
	public static void setUpClass() {
		try {
			Thread.currentThread().sleep(1000L);
		} catch (InterruptedException e) {
		}
	}

	@Test
	public void testConsumer() {
		messageListenerContainer.start();
	}

	@Test
	public void testAck() {
		ackMessageListenerContainer.start();
	}
	
	@Test
	public void testConcurrent() {
		concurrentMessageListenerContainer.start();
	}

}
