package com.raphaelcollin.usermanagement.infrastructure.queue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class BookingMessage {
    @JsonCreator
    public BookingMessage(@JsonProperty("bookingId") int bookingId, @JsonProperty("userId") String userId, @JsonProperty("vehicleId") int vehicleId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.vehicleId = vehicleId;
    }

    @JsonProperty("bookingId")
    int bookingId;

    @JsonProperty("userId")
    String userId;

    @JsonProperty("vehicleId")
    int vehicleId;
}