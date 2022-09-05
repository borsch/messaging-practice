package com.github.borsch.messagingpractice.kafka.taxi.task3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaxiGetController {

    private final TaxiGeoProducer taxiGeoProducer;

    @PostMapping("/taxi-geo")
    public ResponseEntity<Void> taxiGeoLocation(@RequestBody TaxiGeoLocation geoLocation) {
        taxiGeoProducer.produce(geoLocation);
        return ResponseEntity.ok(null);
    }
}
