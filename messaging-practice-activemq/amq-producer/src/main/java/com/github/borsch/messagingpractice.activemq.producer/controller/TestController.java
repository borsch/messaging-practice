package com.github.borsch.messagingpractice.activemq.producer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.borsch.messagingpractice.activemq.producer.producers.Publisher;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final Publisher publisher;

    @GetMapping("/topic")
    public ResponseEntity<String> publishMessageToTopic(@RequestParam("message") String message) {
        publisher.publish(message);
        return ResponseEntity.ok("published");
    }

    @GetMapping("/send-and-receive")
    public ResponseEntity<String> requestAndReceive(@RequestParam("message") String message) {
        return ResponseEntity.ok(publisher.requestReplyQueue(message));
    }

    @GetMapping("/virtual-topic")
    public ResponseEntity<String> publishMessageToVirtualTopic(@RequestParam("message") String message) {
        publisher.publishToVirtualTopic(message);
        return ResponseEntity.ok("published");
    }
}
