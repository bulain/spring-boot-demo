package com.bulain.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = KafkaApplication.class)
public class KafkaSender {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	
    @SuppressWarnings("static-access")
    @Test
	public void testSend() {
		for (int i = 0; i < 100; i++) {
			kafkaTemplate.send("myTopic", "this is a test message " + i);
		}
		
		try {
	        Thread.currentThread().sleep(100L);
        } catch (InterruptedException e) {
        }
		
	}

}
