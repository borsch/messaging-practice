package com.github.borsch.messagingpractice.kafka.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaTaxiConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTaxiConsumerApplication.class, args);
    }

}
