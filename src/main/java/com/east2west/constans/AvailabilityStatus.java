package com.east2west.constans;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AvailabilityStatus {
    AVAILABLE(0),
    BOOKED(1);
    private final int value;
}



