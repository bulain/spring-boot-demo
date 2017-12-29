package com.bulain.rabbit;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class RabbitSpringWorker {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("amqpTemplate2")
	private AmqpTemplate amqpTemplate2;

	@Autowired
	@Qualifier("amqpTemplate4")
	private AmqpTemplate amqpTemplate4;

	public void onMessage(String message) {
		logger.debug("{}", message);

		message = message + "-X";
		
		Random random = new Random();
		int nextInt = random.nextInt();
		if (nextInt % 2 == 0) {
			logger.debug("{}", "send to amqpTemplate2");
			amqpTemplate2.convertAndSend((Object) message, new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws AmqpException {
					return MessageBuilder.fromMessage(message).setExpiration("10000").build();
				}
			});
		} else {
			logger.debug("{}", "send to amqpTemplate4");
			amqpTemplate4.convertAndSend((Object) message);;
		}
	}

}
