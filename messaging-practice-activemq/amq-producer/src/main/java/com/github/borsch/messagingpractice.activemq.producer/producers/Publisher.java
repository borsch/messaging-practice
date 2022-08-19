package com.github.borsch.messagingpractice.activemq.producer.producers;

import java.util.Random;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.github.borsch.messagingpractice.activemq.producer.config.AmqConfig;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Publisher implements ApplicationRunner {

    @Qualifier(AmqConfig.TOPIC_JMS_TEMPLATE)
    private final JmsTemplate topicTemplate;
    @Qualifier(AmqConfig.QUEUE_JMS_TEMPLATE)
    private final JmsTemplate queueTemplate;
    @Value("${topic.durable-subscriber}")
    private final String topic;
    @Value("${queue.with-reply}")
    private final String requestReplyQueue;

    @Override
    public void run(final ApplicationArguments args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            publish("message-" + i);

            Thread.sleep(new Random().nextInt(1000));
        }
    }

    public void publish(String message) {
        topicTemplate.convertAndSend(topic, message);
        log.info("Published message {} to durable topic {}", message, topic);
    }

    @SneakyThrows
    public String requestReplyQueue(String requestMessage) {
        Message message = queueTemplate.sendAndReceive(requestReplyQueue, session -> session.createTextMessage(requestMessage));
        log.info("Sent request message {} and received response {}", requestMessage, message);
        return ((TextMessage) message).getText();
    }
}
