package com.bulain.rabbit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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
	
	@Test
	public void testReceiveObject() {

		RabbitData data = (RabbitData) amqpTemplate.receiveAndConvert("bulain.object.queue");
		logger.debug("{}", data);
	
	}
	
	@Test
	public void testReceiveMessage() {

		Message message = amqpTemplate.receive("bulain.object.queue");
		logger.debug("{}", message);
	
	}
	
	@Test
	public void testReceiveAndReply() {
		ReceiveAndReplyCallback<Object, Object> callback = new ReceiveAndReplyCallback<Object, Object>() {
			@Override
			public Object handle(Object payload) {
				logger.debug("{}", payload);
				return payload + " Reply";
			}
		};
		amqpTemplate.receiveAndReply("bulain.direct.queue", callback);
	}

}
