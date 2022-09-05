package com.github.borsch.messagingpractice.kafka.taxi.task3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiGeoLocation {

    private long taxiId;
    private String lng;
    private String ltd;

}
