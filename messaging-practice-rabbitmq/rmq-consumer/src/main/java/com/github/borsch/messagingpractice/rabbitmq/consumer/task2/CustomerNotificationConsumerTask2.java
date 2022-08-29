package com.github.borsch.messagingpractice.rabbitmq.consumer.task2;

import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerNotificationConsumerTask2 {

    private static final TypeReference<Map<String, Object>> CUSTOMER_NOTIFICATION_PAYLOAD_TYPE = new TypeReference<>() {};

    private final ObjectMapper objectMapper;
    private final AmqpTemplate amqpTemplate;

    @SneakyThrows
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("customer-notifications-task2"),
            key = "customer.receipt",
            exchange = @Exchange("CustomerExchangeTask2")
        )
    )
    public void customerNotificationReceiver(Message<String> message) {
        String payload = message.getPayload();
        log.info("Received new customer notification: {}", payload);

        try {
            objectMapper.readValue(payload, CUSTOMER_NOTIFICATION_PAYLOAD_TYPE);
        } catch (Exception e) {
            log.error("Failed to process message '{}'", payload);

            Integer count = message.getHeaders().get("count", Integer.class);
            if (count != null && count > 2) {
                String failedMessage = objectMapper.writeValueAsString(Map.of(
                    "originalPayload", payload,
                    "errorMessage", e.getMessage()
                ));
                amqpTemplate.convertAndSend("SupportExchangeTask2", "malformed.customer.notification", failedMessage);
            } else {
                log.warn("First attempt for payload '{}'. Try to process in 5 sec again", payload);

                amqpTemplate.convertAndSend("DelayedExchangeTask2", "delayed.messages", message.getPayload(), message1 -> {
                    message1.getMessageProperties().setDelay(5_000);
                    message1.getMessageProperties().setHeader("targetExchange", "CustomerExchangeTask2");
                    message1.getMessageProperties().setHeader("targetKey", "customer.receipt");
                    message1.getMessageProperties().setHeader("count", count == null ? 1 : count + 1);
                    return message1;
                });
            }
        }
    }

}
