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

    @JmsListener(destination = "${queue.with-reply}", containerFactory = AmqConfig.QUEUE_JMS_LISTENER)
    String receiveAndReply(String message) {
        log.info("Receive and reply request: {}", message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "Message - " + message + " has been processed";
    }

    @JmsListener(destination = "Consumer.firstConsumer.${virtual.topic}", containerFactory = AmqConfig.VIRTUAL_TOPIC_JMS_LISTENER)
    void virtualTopicReceiver1(String message) {
        log.info("Virtual topic. First consumer received : {}", message);
    }

}
