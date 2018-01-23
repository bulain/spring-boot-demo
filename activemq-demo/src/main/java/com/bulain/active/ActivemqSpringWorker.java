package com.bulain.active;

import java.util.Random;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public class ActivemqSpringWorker {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("jmsTemplate2")
	private JmsTemplate jmsTemplate2;

	@Autowired
	@Qualifier("jmsTemplate4")
	private JmsTemplate jmsTemplate4;

	public void onMessage(String message) {
		logger.debug("{}", message);

		message = message + "-X";

		Random random = new Random();
		int nextInt = random.nextInt();
		if (nextInt % 2 == 0) {
			logger.debug("{}", "send to jmsTemplate2");
			jmsTemplate2.convertAndSend((Object) message, new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) {
					return message;
				}
			});
		} else {
			logger.debug("{}", "send to jmsTemplate4");
			jmsTemplate4.convertAndSend((Object) message);;
		}
	}

}
