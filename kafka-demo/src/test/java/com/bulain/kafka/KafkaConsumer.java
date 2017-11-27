package com.bulain.kafka;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaApplication.class)
public class KafkaConsumer {

	@Autowired
	private MessageListenerContainer messageListenerContainer;
	@Autowired
	private MessageListenerContainer ackMessageListenerContainer;

	@SuppressWarnings("static-access")
	@AfterClass
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


}
