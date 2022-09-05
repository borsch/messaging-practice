package com.github.borsch.messagingpractice.kafka.taxi.task3;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaxiGeoDataConsumer {

    @KafkaListener(topics = "${kafka.task3.topic}", groupId = "${kafka.task3.group-id}")
    void consumeRecords(ConsumerRecord<String, TaxiGeoLocation> inputMessage) {
        log.info("Receiver message from partition-offset {}-{} and value '{}'", inputMessage.partition(), inputMessage.offset(), inputMessage.value());
    }

}
