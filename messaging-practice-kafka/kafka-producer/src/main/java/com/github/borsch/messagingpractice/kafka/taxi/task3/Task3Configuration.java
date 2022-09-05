package com.github.borsch.messagingpractice.kafka.taxi.task3;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class Task3Configuration {

    @Bean
    KafkaTemplate<String, TaxiGeoLocation> taxiKafkaTemplate(ProducerFactory<String, TaxiGeoLocation> producerFactory,
        ConcurrentKafkaListenerContainerFactory<String, TaxiGeoLocation> listenerFactory) {
        KafkaTemplate<String, TaxiGeoLocation> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        listenerFactory.getContainerProperties().setMissingTopicsFatal(false);
        listenerFactory.setReplyTemplate(kafkaTemplate);

        return kafkaTemplate;
    }

    @Bean
    ApplicationRunner taxiGeoGenerator(TaxiGeoProducer simpleProducer) {
        return args -> {
            new Thread(() -> {
                for (int i = 0; i < 50; i++) {
                    TaxiGeoLocation geoLocation = new TaxiGeoLocation((long) i % 5, String.valueOf(i % 11), String.valueOf(i));

                    simpleProducer.produce(geoLocation);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) { }
                }
            }, "GenThread").start();
        };
    }

    @Bean
    NewTopic taxiGeoTopic(@Value("${kafka.task3.topic}") String topic) {
        return TopicBuilder.name(topic)
            .partitions(3)
            .replicas(3)
            .build();
    }

}
