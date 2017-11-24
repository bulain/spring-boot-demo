package com.bulain.rabbit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitInit {

	@Bean
	public Exchange directExchange() {
		return new DirectExchange("bulain.direct");
	}

	@Bean
	public Queue directQueue() {
		return new Queue("bulain.direct.queue");
	}
	
	@Bean
	public Binding directBinding() {
		String destination = "bulain.direct.queue";
		DestinationType destinationType = DestinationType.QUEUE;
		String exchange = "bulain.direct";
		String routingKey = "direct";
		Map<String, Object> arguments = new HashMap<String, Object>();
		return new Binding(destination, destinationType, exchange, routingKey, arguments);
	}

	@Bean
	public Exchange topicExchange() {
		return new TopicExchange("bulain.topic");
	}
	
	@Bean
	public Queue topicQueue1() {
		return new Queue("bulain.topic.queue1");
	}
	
	@Bean
	public Queue topicQueue2() {
		return new Queue("bulain.topic.queue2");
	}

	@Bean
	public Binding topicBinding1() {
		String destination = "bulain.topic.queue1";
		DestinationType destinationType = DestinationType.QUEUE;
		String exchange = "bulain.topic";
		String routingKey = "topic.*";
		Map<String, Object> arguments = new HashMap<String, Object>();
		return new Binding(destination, destinationType, exchange, routingKey, arguments);
	}
	
	@Bean
	public Binding topicBinding2() {
		String destination = "bulain.topic.queue2";
		DestinationType destinationType = DestinationType.QUEUE;
		String exchange = "bulain.topic";
		String routingKey = "topic.*";
		Map<String, Object> arguments = new HashMap<String, Object>();
		return new Binding(destination, destinationType, exchange, routingKey, arguments);
	}
	
	@Bean
	public Exchange fanoutExchange() {
		return new FanoutExchange("bulain.fanout");
	}

	@Bean
	public Queue fanoutQueue1() {
		return new Queue("bulain.fanout.queue1");
	}

	@Bean
	public Queue fanoutQueue2() {
		return new Queue("bulain.fanout.queue2");
	}
	
	@Bean
	public Binding fanoutBinding1() {
		String destination = "bulain.fanout.queue1";
		DestinationType destinationType = DestinationType.QUEUE;
		String exchange = "bulain.fanout";
		String routingKey = "fanout";
		Map<String, Object> arguments = new HashMap<String, Object>();
		return new Binding(destination, destinationType, exchange, routingKey, arguments);
	}
	
	@Bean
	public Binding fanoutBinding2() {
		String destination = "bulain.fanout.queue2";
		DestinationType destinationType = DestinationType.QUEUE;
		String exchange = "bulain.fanout";
		String routingKey = "fanout";
		Map<String, Object> arguments = new HashMap<String, Object>();
		return new Binding(destination, destinationType, exchange, routingKey, arguments);
	}

	@Bean
	public SimpleMessageListenerContainer topicQueueContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConcurrentConsumers(2);
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("bulain.topic.queue1","bulain.topic.queue2");
		container.setMessageListener(new RabbitMessageListener());
		container.setAutoStartup(false);
		return container;
	}

	@Bean
	public SimpleMessageListenerContainer fanoutQueueContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("bulain.fanout.queue1", "bulain.fanout.queue2");
		container.setMessageListener(new RabbitAckMessageListener());
		container.setAutoStartup(false);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		return container;
	}

}
