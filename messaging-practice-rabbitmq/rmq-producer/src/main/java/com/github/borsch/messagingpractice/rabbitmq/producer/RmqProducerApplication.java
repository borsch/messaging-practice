package com.github.borsch.messagingpractice.rabbitmq.producer;

import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class RmqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmqProducerApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(AmqpTemplate amqpTemplate, ObjectMapper objectMapper) {
        return args -> {
            for (int i = 0; i < 3; i++) {
                Map<String, Object> message = Map.of(
                    "customerName", "Customer" + i,
                    "customerEmail", "email" + i,
                    "totalInvoiceSum", 10 * i
                );

                amqpTemplate.convertAndSend("CustomerExchangeTask1", "customer.receipt", objectMapper.writeValueAsString(message));
                amqpTemplate.convertAndSend("CustomerExchangeTask2", "customer.receipt", objectMapper.writeValueAsString(message));
            }

            amqpTemplate.convertAndSend("CustomerExchangeTask1", "customer.receipt", "malformed message");
            amqpTemplate.convertAndSend("CustomerExchangeTask2", "customer.receipt", "malformed message");
        };
    }
}
