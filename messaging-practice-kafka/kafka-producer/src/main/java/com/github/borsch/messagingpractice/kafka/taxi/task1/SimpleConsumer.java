package com.github.borsch.messagingpractice.kafka.taxi.task1;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SimpleConsumer {

    @KafkaListener(topics = "${kafka.task1.topic}", groupId = "${kafka.task1.group-id}")
    void consumeRecords(ConsumerRecord<String, String> inputMessage) {
        log.info("Receiver message from partition-offset {}-{} and value '{}'", inputMessage.partition(), inputMessage.offset(), inputMessage.value());
    }

}
