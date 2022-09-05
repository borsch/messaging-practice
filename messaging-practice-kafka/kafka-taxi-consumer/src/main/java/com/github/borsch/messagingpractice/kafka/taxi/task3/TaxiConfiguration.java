package com.github.borsch.messagingpractice.kafka.taxi.task3;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TaxiConfiguration {

    @Bean
    NewTopic taxiGeoTopic(@Value("${kafka.task3.topic}") String topic) {
        return TopicBuilder.name(topic)
            .partitions(3)
            .replicas(3)
            .build();
    }

}
