package com.github.borsch.messagingpractice.kafkastreams;

import java.time.Duration;
import java.util.Objects;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class Task3Configuration {

    @Value("${kafka.topics.task3-1}")
    private final String sourceOne;
    @Value("${kafka.topics.task3-2}")
    private final String sourceTwo;

    @Bean
    KStream<Long, String> task3Stream1(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream(sourceOne, Consumed.with(Serdes.String(), Serdes.String()))
            .peek((key, value) -> log.info("Source 1 message: {} - {}", key, value))
            .filter((key, value) -> Objects.nonNull(value))
            .filter((key, value) -> value.contains(":"))
            .map((key, value) -> {
                String[] parts = value.split(":");
                return new KeyValue<>(Long.parseLong(parts[0]), parts[1]);
            })
            .peek((key, value) -> log.info("Processed messages from source 1: {} - {}", key, value));
    }

    @Bean
    KStream<Long, String> task3Stream2(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream(sourceTwo, Consumed.with(Serdes.String(), Serdes.String()))
            .peek((key, value) -> log.info("Source 2 message: {} - {}", key, value))
            .filter((key, value) -> Objects.nonNull(value))
            .filter((key, value) -> value.contains(":"))
            .map((key, value) -> {
                String[] parts = value.split(":");
                return new KeyValue<>(Long.parseLong(parts[0]), parts[1]);
            })
            .peek((key, value) -> log.info("Processed messages from source 2: {} - {}", key, value));
    }

    @Bean
    KStream<Long, String> joinedStreams(KStream<Long, String> task3Stream1, KStream<Long, String> task3Stream2) {
        return task3Stream1.join(
                task3Stream2,
                (lValue, rValue) -> lValue + " - " + rValue,
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(1L)),
                StreamJoined
                    .with(Serdes.Long(), Serdes.String(), Serdes.String())
                    .withName("streams_joiner")
            )
            .peek((key, value) -> log.info("Joined value for key '{}' is '{}'", key, value));
    }

    @Bean(name = "task3InitMessagesProducer")
    ApplicationRunner task1InitMessagesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send(sourceOne, "1:1part1");
            kafkaTemplate.send(sourceOne, "2:2part1");
            kafkaTemplate.send(sourceOne, "3:3part1");
            kafkaTemplate.send(sourceOne, "4:4part1-without-second-part");

            kafkaTemplate.send(sourceTwo, "1:1part2");
            Thread.sleep(30_000);
            kafkaTemplate.send(sourceTwo, "2:2part2");
            kafkaTemplate.send(sourceTwo, "5:5part2-without-first-part");
            kafkaTemplate.send(sourceTwo, "6:6part2-without-first-part");
            kafkaTemplate.send(sourceTwo, "3:3part2");
        };
    }

}
