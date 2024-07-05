package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyFactory;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.With;

import java.time.LocalDateTime;

public record BookingRequest(
        @Min(value = 1, message = "Vehicle ID must be greater than 0")
        int vehicleId,

        @With
        String userId,

        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @Future(message = "End time must be in the future")
        LocalDateTime endTime
) {
    public Booking createBooking() {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setVehicleId(vehicleId);
        booking.setStatus(Booking.Status.CREATED);
        booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        return booking;
    }
}
