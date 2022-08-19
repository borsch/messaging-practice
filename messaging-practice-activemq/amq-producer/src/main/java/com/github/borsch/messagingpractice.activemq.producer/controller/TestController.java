package com.github.borsch.messagingpractice.activemq.producer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.borsch.messagingpractice.activemq.producer.producers.TopicPublisher;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TopicPublisher topicPublisher;

    @GetMapping("/topic")
    public ResponseEntity<String> publishMessageToTopic(
        @RequestParam("message") String message
    ) {
        topicPublisher.publish(message);
        return ResponseEntity.ok("published");
    }

}
