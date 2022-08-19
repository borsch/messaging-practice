package com.github.borsch.messagingpractice.activemq.producer.producers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.github.borsch.messagingpractice.activemq.producer.config.AmqConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicPublisher implements ApplicationRunner {

    @Qualifier(AmqConfig.TOPIC_JMS_TEMPLATE)
    private final JmsTemplate jmsTemplate;
    @Value("${topic.durable-subscriber}")
    private final String topic;

    @Override
    public void run(final ApplicationArguments args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            publish("message-" + i);

            Thread.sleep(new Random().nextInt(1000) + 1000);
        }
    }

    public void publish(String message) {
        jmsTemplate.convertAndSend(topic, message);
        log.info("Published message {} to durable topic {}", message, topic);
    }
}
