package com.github.borsch.messagingpractice.kafkastreams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class Task1Configuration {

    @Value("${kafka.topics.task1-1}")
    private final String source;
    @Value("${kafka.topics.task1-2}")
    private final String target;

    @Bean
    KStream<String, String> task1Stream(StreamsBuilder streamsBuilder) {
        KStream<String, String> kStream = streamsBuilder.stream(source, Consumed.with(Serdes.String(), Serdes.String()));
        kStream.peek((key, value) -> log.info("Processing message: {} - {}", key, value));
        kStream.to(target);
        return kStream;
    }

    @Bean(name = "task1InitMessagesProducer")
    ApplicationRunner task1InitMessagesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send(source, "message1");
            kafkaTemplate.send(source, "message2");
            kafkaTemplate.send(source, "message3");
            kafkaTemplate.send(source, "message4");
        };
    }

    @KafkaListener(topics = "${kafka.topics.task1-2}", groupId = "task1")
    void task1Consumer(String message) {
        log.info("Consumed message: {}", message);
    }

}
