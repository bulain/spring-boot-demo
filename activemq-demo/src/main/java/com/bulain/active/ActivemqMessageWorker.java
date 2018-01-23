package com.bulain.active;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivemqMessageWorker {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void handleMessage(String message) {
		logger.debug("{}", message);
	}

}
