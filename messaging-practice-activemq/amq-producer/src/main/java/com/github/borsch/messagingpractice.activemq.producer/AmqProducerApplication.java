package com.github.borsch.messagingpractice.activemq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmqProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmqProducerApplication.class, args);
	}

}
