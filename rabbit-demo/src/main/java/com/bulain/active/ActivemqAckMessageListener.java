package com.bulain.active;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivemqAckMessageListener implements MessageListener {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message message) {
		logger.debug("{}", message);
		try {
			message.acknowledge();
		} catch (JMSException e) {
		}
	}

}
