package com.bulain.active;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class ActivemqConsumer {

	@Autowired
	@Qualifier("queueContainer1")
	private SimpleMessageListenerContainer queueContainer1;
	@Autowired
	@Qualifier("queueContainer2")
	private SimpleMessageListenerContainer queueContainer2;
	@Autowired
	@Qualifier("queueContainer3")
	private SimpleMessageListenerContainer queueContainer3;

	@AfterAll
	public static void tearDownClass(){
		try {
			Thread.currentThread().sleep(2000L);
		} catch (InterruptedException e) {
		}
	}
	
	@Test
	public void testStartQueue1() {
		queueContainer1.start();
	}
	
	@Test
	public void testStartQueue2() {
		queueContainer2.start();
	}

	@Test
	public void testStartQueue3() {
		queueContainer3.start();
	}

}
