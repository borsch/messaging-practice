package com.github.borsch.messagingpractice.kafkastreams;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class Task4Configuration {

    @Value("${kafka.topics.task4}")
    private final String source;

    @Bean
    KStream<String, UserDto> task4Stream(StreamsBuilder streamsBuilder) {
        final Serde<UserDto> userDtoSerde = Serdes.serdeFrom(new UserDtoSerializer(), new UserDtoDeserializer());

        KStream<String, UserDto> kStream = streamsBuilder.stream(source, Consumed.with(Serdes.String(), userDtoSerde));
        kStream.peek((key, value) -> log.info("Processing message: {} - {}", key, value));
        return kStream;
    }

    @Bean(name = "task4InitMessagesProducer")
    ApplicationRunner task1InitMessagesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send(source, "invalid user");
            kafkaTemplate.send(source, """
                {"name":"John","company":"EPAM","position":"developer","experience":5}
                """);
            kafkaTemplate.send(source, "invalid user2");
            kafkaTemplate.send(source, """
                {"name":"Dev2","company":"EPAM","position":"developer","experience":5}
                """);
        };
    }

    private static class UserDtoSerializer implements Serializer<UserDto> {

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        @Override
        @SneakyThrows
        public byte[] serialize(final String s, final UserDto userDto) {
            return OBJECT_MAPPER.writeValueAsBytes(userDto);
        }
    }

    private static class UserDtoDeserializer implements Deserializer<UserDto> {

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        @Override
        @SneakyThrows
        public UserDto deserialize(final String topic, final byte[] data) {
            return OBJECT_MAPPER.readValue(data, UserDto.class);
        }
    }

    @Data
    private static class UserDto {
        private String name;
        private String company;
        private String position;
        private int experience;
    }
}
