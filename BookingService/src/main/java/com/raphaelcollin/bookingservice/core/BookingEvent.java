package com.raphaelcollin.bookingservice.core;

import lombok.Value;

@Value
public class BookingEvent {
    int bookingId;
    String userId;
    int vehicleId;
}
