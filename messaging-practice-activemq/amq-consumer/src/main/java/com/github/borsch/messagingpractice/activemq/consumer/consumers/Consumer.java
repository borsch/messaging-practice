package com.github.borsch.messagingpractice.activemq.consumer.consumers;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.github.borsch.messagingpractice.activemq.consumer.config.AmqConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Consumer {

    @JmsListener(destination = "${topic.durable-subscriber}", containerFactory = AmqConfig.TOPIC_JMS_LISTENER_DURABLE)
    void durableJmsListener(String message) {
        log.info("Durable subscriber: received message {}", message);
    }

    @JmsListener(destination = "${topic.durable-subscriber}", containerFactory = AmqConfig.TOPIC_JMS_LISTENER_NON_DURABLE)
    void nonDurableJmsListener(String message) {
        log.info("Non-durable subscriber: received message {}", message);
    }

}
