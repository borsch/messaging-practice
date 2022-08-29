package com.github.borsch.messagingpractice.rabbitmq.consumer.task2;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelayedMessageConsumerTask2 {

    private final AmqpTemplate amqpTemplate;

    @SneakyThrows
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("delayed-message-task2"),
            key = "delayed.messages",
            exchange = @Exchange(value = "DelayedExchangeTask2", delayed = "true")
        )
    )
    public void customerNotificationReceiver(Message<String> message) {
        String targetExchange = message.getHeaders().get("targetExchange", String.class);
        String targetKey = message.getHeaders().get("targetKey", String.class);

        if (targetExchange == null || targetKey == null) {
            log.error("Reject message '{}' as it doesn't contains neither target exchange not target queue", message);
            throw new AmqpRejectAndDontRequeueException("Reject message as it doesn't contains neither target exchange not target queue");
        }

        amqpTemplate.convertAndSend(targetExchange, targetKey, message.getPayload(), message1 -> {
            message1.getMessageProperties().setHeader("count", message.getHeaders().get("count"));
            return message1;
        });
    }

}
