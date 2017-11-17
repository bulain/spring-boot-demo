package com.bulain.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListen {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@KafkaListener(topics = "myTopic")
	public void listen(String content) {
		logger.debug("listen- {}", content);

	}

}
