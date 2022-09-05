package com.github.borsch.messagingpractice.kafka.taxi.task3;

import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaxiGeoProducer {

    private final KafkaTemplate<String, TaxiGeoLocation> taxiKafkaTemplate;
    @Value("${kafka.task3.topic}")
    private final String topic;

    @SneakyThrows
    public void produce(TaxiGeoLocation taxiGetLocation) {
        ProducerRecord<String, TaxiGeoLocation> record = new ProducerRecord<>(topic, String.valueOf(taxiGetLocation.getTaxiId()), taxiGetLocation);

        SendResult<String, TaxiGeoLocation> sendResult = taxiKafkaTemplate.send(record)
            .get(5, TimeUnit.SECONDS);
        log.info("Sending message '{}' result - {}", taxiGetLocation, sendResult);
    }

}
