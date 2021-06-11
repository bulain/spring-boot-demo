package com.bulain.active;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivemqMessageListener implements MessageListener {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message message) {
		logger.debug("{}", message);
	}

}
