package com.github.borsch.messagingpractice.rabbitmq.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SupportMessagesConsumer {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("all-support-notifications"),
            key = "#",
            exchange = @Exchange(value = "SupportExchange", type = ExchangeTypes.TOPIC)
        )
    )
    public void allMessageSupportHandler(Message<String> message) {
        log.error("GLOBAL SUPPORT TEAM: Received support notification {}. Handling it ..", message.getPayload());
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("invalid-orders"),
            key = "*.customer.notification",
            exchange = @Exchange(value = "SupportExchange", type = ExchangeTypes.TOPIC)
        )
    )
    public void customerNotificationReceiver(Message<String> message) {
        log.error("SALES SUPPORT TEAM: Received invalid customer notification {}. Sales department with handle this ..", message.getPayload());
    }
}
