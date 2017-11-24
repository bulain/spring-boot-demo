package com.bulain.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class RabbitReceiver {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Test
	public void testReceive() {
		Object message = amqpTemplate.receiveAndConvert("bulain.direct.queue");
		logger.debug("{}", message);
	}

}
