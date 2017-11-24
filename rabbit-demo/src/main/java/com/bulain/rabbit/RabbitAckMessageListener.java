package com.bulain.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class RabbitAckMessageListener implements ChannelAwareMessageListener  {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		logger.debug("{}", message);
		
		MessageProperties messageProperties = message.getMessageProperties();
		channel.basicAck(messageProperties.getDeliveryTag(), false);
	}

}
