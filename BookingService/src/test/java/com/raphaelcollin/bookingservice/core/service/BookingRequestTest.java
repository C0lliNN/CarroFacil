package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestTest {

    @Test
    void testBooking_createBooking() {
        BookingRequest bookingRequest = new BookingRequest(
                1,
                "user1",
                LocalDateTime.parse("2021-01-01T00:00:00"),
                LocalDateTime.parse("2021-01-01T01:00:00")
        );

        Booking booking = bookingRequest.createBooking();

        assertEquals("user1", booking.getUserId());
        assertEquals(1, booking.getVehicleId());
        assertEquals("2021-01-01T00:00", booking.getStartTime().toString());
        assertEquals("2021-01-01T01:00", booking.getEndTime().toString());
        assertEquals(booking.getStatus(), Booking.Status.CREATED);
        assertEquals(booking.getPricingStrategy().getType(), PricingStrategyType.HOURLY);
    }

}