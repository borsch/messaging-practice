package com.github.borsch.messagingpractice.activemq.consumer.consumers;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.github.borsch.messagingpractice.activemq.consumer.config.AmqConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Consumer {

    @JmsListener(destination = "Consumer.secondsConsumer.${virtual.topic}", containerFactory = AmqConfig.VIRTUAL_TOPIC_JMS_LISTENER)
    void virtualTopicReceiver2(String message) {
        log.info("Virtual topic. Second consumer received : {}", message);
    }

}
