package com.bulain.active;

import javax.jms.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class ActivemqReceiver {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("jmsTemplate1")
	private JmsTemplate jmsTemplate;

	@Test
	public void testReceive() {
		Object message = jmsTemplate.receiveAndConvert("bulain.queue1");
		logger.debug("{}", message);
	}

	@Test
	public void testReceiveObject() {

		ActivemqData data = (ActivemqData) jmsTemplate.receiveAndConvert("bulain.queue1");
		logger.debug("{}", data);

	}

	@Test
	public void testReceiveMessage() {

		Message message = jmsTemplate.receive("bulain.queue1");
		logger.debug("{}", message);

	}

}
