package com.github.borsch.messagingpractice.rabbitmq.consumer.task2;

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
public class SupportMessagesConsumerTask2 {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("all-support-notifications-task2"),
            key = "#",
            exchange = @Exchange(value = "SupportExchangeTask2", type = ExchangeTypes.TOPIC)
        )
    )
    public void allMessageSupportHandler(Message<String> message) {
        log.error("GLOBAL SUPPORT TEAM: Received support notification {}. Handling it ..", message.getPayload());
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("invalid-orders-task2"),
            key = "*.customer.notification",
            exchange = @Exchange(value = "SupportExchangeTask2", type = ExchangeTypes.TOPIC)
        )
    )
    public void customerNotificationReceiver(Message<String> message) {
        log.error("SALES SUPPORT TEAM: Received invalid customer notification {}. Sales department with handle this ..", message.getPayload());
    }
}
