package com.bulain.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

public class KafkaAckMessageListener implements AcknowledgingMessageListener<String, Object> {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(ConsumerRecord<String, Object> data, Acknowledgment acknowledgment) {
		logger.debug("{}", data);

		acknowledgment.acknowledge();
	}

}
