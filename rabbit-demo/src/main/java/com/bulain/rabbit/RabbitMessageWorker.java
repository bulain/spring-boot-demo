package com.bulain.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMessageWorker {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void handleMessage(String message) {
		logger.debug("{}", message);
	}

}
