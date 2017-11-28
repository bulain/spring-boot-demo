package com.bulain.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

@Configuration
public class KafkaInit {

	@Bean
	public MessageListenerContainer messageListenerContainer(ConsumerFactory<String, Object> consumerFactory) {
		ContainerProperties containerProperties = new ContainerProperties("myTopic");
		KafkaMessageListenerContainer<String, Object> container = new KafkaMessageListenerContainer<String, Object>(
				consumerFactory, containerProperties);
		container.setAutoStartup(false);
		container.setupMessageListener(new KafkaMessageListener());
		return container;
	}

	@Bean
	public MessageListenerContainer ackMessageListenerContainer(ConsumerFactory<String, Object> consumerFactory) {
		ContainerProperties containerProperties = new ContainerProperties("ackTopic");
		containerProperties.setAckMode(AckMode.MANUAL);
		KafkaMessageListenerContainer<String, Object> container = new KafkaMessageListenerContainer<String, Object>(
				consumerFactory, containerProperties);
		container.setAutoStartup(false);
		container.setupMessageListener(new KafkaAckMessageListener());
		return container;
	}
	
	@Bean
	public MessageListenerContainer concurrentMessageListenerContainer(ConsumerFactory<String, Object> consumerFactory) {
		ContainerProperties containerProperties = new ContainerProperties("concurrentTopic");
		ConcurrentMessageListenerContainer<String, Object> container = new ConcurrentMessageListenerContainer<String, Object>(
				consumerFactory, containerProperties);
		container.setAutoStartup(false);
		container.setConcurrency(3);
		container.setupMessageListener(new KafkaMessageListener());
		return container;
	}

}
