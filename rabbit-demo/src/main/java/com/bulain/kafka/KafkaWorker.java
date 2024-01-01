package com.bulain.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaWorker {

    @KafkaListener(topics = {"ackTopic"})
    public void onMessage(ConsumerRecord<String, Object> data, Acknowledgment acknowledgment) {
        log.debug("{}", data);

        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = {"myTopic", "concurrentTopic"})
    public void onMessage(ConsumerRecord<String, Object> data) {
        log.debug("{}", data);
    }

}
