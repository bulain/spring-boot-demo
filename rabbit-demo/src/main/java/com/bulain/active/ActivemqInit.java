package com.bulain.active;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

@Configuration
public class ActivemqInit {

	@Bean
	public JmsTemplate jmsTemplate1(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("bulain.queue1");
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplate2(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("bulain.queue2");
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplate3(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("bulain.queue3");
		return jmsTemplate;
	}
	
	@Bean
	public JmsTemplate jmsTemplate4(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("bulain.queue4");
		return jmsTemplate;
	}

	@Bean
	public SimpleMessageListenerContainer queueContainer1(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConcurrentConsumers(3);
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName("bulain.queue1");
		container.setMessageListener(new ActivemqMessageListener());
		container.setAutoStartup(false);
		container.setConnectLazily(true);
		return container;
	}

	@Bean
	public SimpleMessageListenerContainer queueContainer2(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConcurrentConsumers(1);
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName("bulain.queue2");
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
		messageListenerAdapter.setDelegate(new ActivemqMessageWorker());
		container.setMessageListener(messageListenerAdapter);
		container.setAutoStartup(false);
		container.setConnectLazily(true);
		return container;
	}

	@Bean
	public SimpleMessageListenerContainer queueContainer3(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName("bulain.queue3");
		container.setMessageListener(new ActivemqAckMessageListener());
		container.setAutoStartup(false);
		container.setConnectLazily(true);
		container.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		return container;
	}

}
