package com.raphaelcollin.bookingservice.core.pricing;

import com.raphaelcollin.bookingservice.core.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HourlyPricingStrategyTest {

    @Test
    @DisplayName("should return 0 when duration is 0")
    void shouldReturn0WhenDurationIs0() {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now());

        HourlyPricingStrategy hourlyPricingStrategy = new HourlyPricingStrategy(10);

        double price = hourlyPricingStrategy.calculatePrice(booking);

        assertEquals(0, price);
    }

    @Test
    @DisplayName("should return 10 when duration is 1")
    void shouldReturn10WhenDurationIs1() {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));

        HourlyPricingStrategy hourlyPricingStrategy = new HourlyPricingStrategy(10);

        double price = hourlyPricingStrategy.calculatePrice(booking);

        assertEquals(10, price);
    }

}