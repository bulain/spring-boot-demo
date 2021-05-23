package com.bulain.rabbit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RabbitApplication.class)
public class RabbitSender {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Test
	public void testSendByRoutingKeySp() {
		for (int i = 0; i < 100; i++) {
			amqpTemplate.convertAndSend("bulain.topic", "topic.1", "this is a topic.1 message " + i);
		}
	}

	@Test
	public void testSendByRoutingKey() {

		amqpTemplate.convertAndSend("bulain.direct", "direct", "this is a direct.1 message");
		amqpTemplate.convertAndSend("bulain.topic", "topic.1", "this is a topic.1 message");
		amqpTemplate.convertAndSend("bulain.topic", "topic.2", "this is a topic.2 message");
		amqpTemplate.convertAndSend("bulain.fanout", "fanout", "this is a fanout.1 message");
		amqpTemplate.convertAndSend("bulain.fanout", "fanout", "this is a fanout.2 message");

	}

	@Test
	public void testSendByQueue() {

		amqpTemplate.convertAndSend("bulain.direct.queue", "this is a direct.2 message");
		amqpTemplate.convertAndSend("bulain.topic.queue1", "this is a topic.3 message");
		amqpTemplate.convertAndSend("bulain.topic.queue2", "this is a topic.4 message");
		amqpTemplate.convertAndSend("bulain.fanout.queue1", "this is a fanout.3 message");
		amqpTemplate.convertAndSend("bulain.fanout.queue2", "this is a fanout.4 message");

	}

	@Test
	public void testSendObject() {
		RabbitData data = new RabbitData();
		data.setId("1");
		data.setName("test");
		data.setDescr("descr");
		amqpTemplate.convertAndSend("bulain.object.queue", data);
	}

	
	@Test
	public void testConvertSendAndReceive() {
		Object msg = amqpTemplate.convertSendAndReceive("bulain.direct", "direct", "this is a direct.3 message");
		logger.debug("{}", msg);
	}

	@Test
	public void testSendAndReceive() {
		byte[] body = "this is a direct.4 message".getBytes();
		Message message = MessageBuilder.withBody(body).setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
				.build();
		Message msg = amqpTemplate.sendAndReceive("bulain.direct", "direct", message);
		logger.debug("{}", msg);
	}

}
