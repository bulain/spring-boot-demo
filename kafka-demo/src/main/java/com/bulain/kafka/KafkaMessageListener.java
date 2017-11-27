package com.bulain.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class KafkaMessageListener implements MessageListener<String, Object> {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(ConsumerRecord<String, Object> data) {
		logger.debug("{}", data);
	}

}
