package com.raphaelcollin.bookingservice.core;

import com.raphaelcollin.bookingservice.core.exception.InvalidBookingStateException;
import com.raphaelcollin.bookingservice.core.pricing.BookingPricingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    int id;
    String userId;
    int vehicleId;
    Vehicle vehicle;
    Status status;
    BookingPricingStrategy pricingStrategy;
    LocalDateTime startTime;
    LocalDateTime endTime;
    LocalDateTime createdAt;
    LocalDateTime checkedInAt;
    LocalDateTime checkedOutAt;

    public enum Status {
        CREATED,
        CANCELLED,
        IN_PROGRESS,
        CLOSED,
    }

    public void checkIn() {
        if (status != Status.CREATED) {
            throw new InvalidBookingStateException("Booking is not in CREATED state");
        }

        status = Status.IN_PROGRESS;
        checkedInAt = LocalDateTime.now();
    }

    public void checkOut() {
        if (status != Status.IN_PROGRESS) {
            throw new InvalidBookingStateException("Booking is not in IN_PROGRESS state");
        }

        status = Status.CLOSED;
        checkedOutAt = LocalDateTime.now();
    }

    public double getPrice() {
        return pricingStrategy.calculatePrice(this);
    }
}
