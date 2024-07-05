package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.Vehicle;
import com.raphaelcollin.bookingservice.core.pricing.BookingPricingStrategy;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyFactory;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingResponseTest {

    @Test
    void testFrom() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setUserId("user1");
        booking.setVehicle(new Vehicle(1, "brand1", "model1", 2021));
        booking.setStartTime(LocalDateTime.parse("2021-01-01T00:00:00"));
        booking.setEndTime(LocalDateTime.parse("2021-01-01T01:00:00"));
        booking.setStatus(Booking.Status.CREATED);
        BookingPricingStrategy pricingStrategy = PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY);
        booking.setPricingStrategy(pricingStrategy);
        booking.setCreatedAt(LocalDateTime.parse("2021-01-01T00:00:00"));

        BookingResponse actual = BookingResponse.from(booking);
        BookingResponse expected = new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                VehicleResponse.fromVehicle(new Vehicle(1, "brand1", "model1", 2021)),
                booking.getStatus().name(),
                booking.getPrice(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getCreatedAt(),
                booking.getCheckedInAt(),
                booking.getCheckedOutAt()
        );

        assertEquals(expected, actual);
    }
}