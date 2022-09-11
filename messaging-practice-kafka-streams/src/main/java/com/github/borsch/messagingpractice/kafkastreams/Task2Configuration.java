package com.github.borsch.messagingpractice.kafkastreams;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
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
public class Task2Configuration {

    @Value("${kafka.topics.task2}")
    private final String source;

    @Bean
    Map<String, KStream<Integer, String>> task2Stream(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream(source, Consumed.with(Serdes.String(), Serdes.String()))
            .filter((key, value) -> Objects.nonNull(value))
            .peek((key, value) -> log.info("Process sentence {}", value))
            .flatMap(
                (key, value) -> Arrays.stream(value.split("\\s+"))
                    .map(word -> new KeyValue<>(word.length(), word))
                    .collect(Collectors.toList())
            )
            .peek((key, value) -> log.info("Split output {} - {}", key, value))
            .split(Named.as("word"))
            .branch((key, value) -> key > 10, Branched.as("-long"))
            .defaultBranch(Branched.as("-short"));
    }

    @Bean
    Map<String, KStream<Integer, String>> task2StreamFilterForLetterA(Map<String, KStream<Integer, String>> task2Stream) {
        task2Stream.get("word-short")
            .filter((key, value) -> value.contains("a"))
            .peek((key, value) -> log.info("Word '{}' from short stream contains 'a'. Move further", value));
        task2Stream.get("word-long")
            .filter((key, value) -> value.contains("a"))
            .peek((key, value) -> log.info("Word '{}' from long stream contains 'a'. Move further", value));

        return task2Stream;
    }

    @Bean(name = "finalStreamsMerger")
    ApplicationRunner finalStreamsMerger(Map<String, KStream<Integer, String>> task2StreamFilterForLetterA) {
        return args -> {
            KStream<Integer, String> kStream = task2StreamFilterForLetterA.get("word-short")
                .merge(task2StreamFilterForLetterA.get("word-long"));

            kStream.foreach((key, value) -> log.info("Final result {} - {}", key, value));
        };
    }

    @Bean(name = "task2InitMessagesProducer")
    ApplicationRunner task2InitMessagesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send(source, "message1 234");
            kafkaTemplate.send(source, "message2 234 234 234 23423523523 235 23523 3 3 4 3 2");
            kafkaTemplate.send(source, "message3 awega wg awegawgeawegwaeg wag waeg awe g");
            kafkaTemplate.send(source, "message4 awg awgwaeg awegw gaweg awe g");
        };
    }
}
