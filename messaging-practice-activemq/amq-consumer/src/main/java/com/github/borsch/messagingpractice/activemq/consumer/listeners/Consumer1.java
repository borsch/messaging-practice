package com.github.borsch.messagingpractice.activemq.consumer.listeners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer1 {

    @JmsListener(destination = "asd")
    void receiveMessage() {

    }

}
