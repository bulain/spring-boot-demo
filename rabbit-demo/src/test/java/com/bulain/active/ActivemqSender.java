package com.bulain.active;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class ActivemqSender {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JmsTemplate jmsTemplate1;

	@Test
	public void testSendMessage() {
		for (int i = 0; i < 1; i++) {
			logger.debug("send 1k * {}", i);
			for (int j = 0; j < 1000; j++) {
				jmsTemplate1.convertAndSend("bulain.queue1", "this is a queue1 message " + i + " - " + j);
			}
		}
	}

}
