package com.github.borsch.messagingpractice.kafkastreams.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    NewTopic task11(@Value("${kafka.topics.task1-1}") String topic) {
        return createTopic(topic);
    }

    @Bean
    NewTopic task12(@Value("${kafka.topics.task1-2}") String topic) {
        return createTopic(topic);
    }

    @Bean
    NewTopic task2(@Value("${kafka.topics.task2}") String topic) {
        return createTopic(topic);
    }

    @Bean
    NewTopic task31(@Value("${kafka.topics.task3-1}") String topic) {
        return createTopic(topic);
    }

    @Bean
    NewTopic task32(@Value("${kafka.topics.task3-2}") String topic) {
        return createTopic(topic);
    }

    @Bean
    NewTopic task4(@Value("${kafka.topics.task4}") String topic) {
        return createTopic(topic);
    }

    private NewTopic createTopic(String topic) {
        return TopicBuilder.name(topic)
            .partitions(1)
            .replicas(1)
            .build();
    }

}
