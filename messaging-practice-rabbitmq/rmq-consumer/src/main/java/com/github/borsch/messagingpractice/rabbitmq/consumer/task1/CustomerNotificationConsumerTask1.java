package com.github.borsch.messagingpractice.rabbitmq.consumer.task1;

import java.util.Map;

import org.springframework.amqp.ImmediateRequeueAmqpException;
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
public class CustomerNotificationConsumerTask1 {

    private static final TypeReference<Map<String, Object>> CUSTOMER_NOTIFICATION_PAYLOAD_TYPE = new TypeReference<Map<String, Object>>() {};

    private final ObjectMapper objectMapper;
    private final AmqpTemplate amqpTemplate;

    @SneakyThrows
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("customer-notifications-task1"),
            key = "customer.receipt",
            exchange = @Exchange("CustomerExchangeTask1")
        )
    )
    public void customerNotificationReceiver(Message<String> message) {
        String payload = message.getPayload();
        log.info("Received new customer notification: {}", payload);

        try {
            objectMapper.readValue(payload, CUSTOMER_NOTIFICATION_PAYLOAD_TYPE);
        } catch (Exception e) {
            log.error("Failed to process message '{}'", payload);

            Boolean redelivered = message.getHeaders().get("amqp_redelivered", Boolean.class);
            if (redelivered == Boolean.TRUE) {
                String failedMessage = objectMapper.writeValueAsString(Map.of(
                    "originalPayload", payload,
                    "errorMessage", e.getMessage()
                ));
                amqpTemplate.convertAndSend("SupportExchangeTask1", "malformed.customer.notification", failedMessage);
            } else {
                log.warn("First attempt for payload '{}'. Try to process again", payload);
                throw new ImmediateRequeueAmqpException("try to re-queue");
            }
        }
    }

}
