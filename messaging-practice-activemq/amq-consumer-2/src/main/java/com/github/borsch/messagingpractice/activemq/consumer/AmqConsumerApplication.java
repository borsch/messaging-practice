package com.github.borsch.messagingpractice.activemq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmqConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmqConsumerApplication.class, args);
	}

}
