package com.github.borsch.messagingpractice.kafka.taxi.task1;

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
public class Task1Configuration {

    @Bean
    KafkaTemplate<String, String> replyTemplate(ProducerFactory<String, String> producerFactory,
        ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        listenerFactory.getContainerProperties().setMissingTopicsFatal(false);
        listenerFactory.setReplyTemplate(kafkaTemplate);

        return kafkaTemplate;
    }

    @Bean
    ApplicationRunner applicationRunner(SimpleProducer simpleProducer) {
        return args -> {
            new Thread(() -> {
                for (int i = 0; i < 50; i++) {
                    simpleProducer.produce("message " + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) { }
                }
            }, "GenThread").start();
        };
    }

    @Bean
    public NewTopic compactTopicExample(@Value("${kafka.task1.topic}") String topic) {
        return TopicBuilder.name(topic)
            .partitions(3)
            .replicas(3)
            .build();
    }

}
