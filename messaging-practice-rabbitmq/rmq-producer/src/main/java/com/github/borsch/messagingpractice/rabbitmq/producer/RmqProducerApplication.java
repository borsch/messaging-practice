package com.github.borsch.messagingpractice.rabbitmq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RmqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmqProducerApplication.class, args);
    }
}
