package com.raphaelcollin.bookingservice.core;

import com.raphaelcollin.bookingservice.core.exception.InvalidBookingStateException;
import com.raphaelcollin.bookingservice.core.pricing.BookingPricingStrategy;
import com.raphaelcollin.bookingservice.core.pricing.HourlyPricingStrategy;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Nested
    @DisplayName("checkIn method")
    class CheckInMethod {

        @Test
        @DisplayName("should change status to IN_PROGRESS and set checkedInAt to current time when status is CREATED")
        void shouldChangeStatusToInProgressAndSetCheckedInAtToCurrentTimeWhenStatusIsCreated() {
            Booking booking = new Booking();
            booking.setStatus(Booking.Status.CREATED);

            booking.checkIn();

            assertEquals(Booking.Status.IN_PROGRESS, booking.getStatus());
            assertNotNull(booking.getCheckedInAt());
        }

        @Test
        @DisplayName("should throw InvalidBookingStateException when status is not CREATED")
        void shouldThrowInvalidBookingStateExceptionWhenStatusIsNotCreated() {
            Booking booking = new Booking();
            booking.setStatus(Booking.Status.CANCELLED);

            assertThrows(InvalidBookingStateException.class, booking::checkIn);
        }
    }

    @Nested
    @DisplayName("checkOut method")
    class CheckOutMethod {

        @Test
        @DisplayName("should change status to CLOSED and set checkedOutAt to current time when status is IN_PROGRESS")
        void shouldChangeStatusToClosedAndSetCheckedOutAtToCurrentTimeWhenStatusIsInProgress() {
            Booking booking = new Booking();
            booking.setStatus(Booking.Status.IN_PROGRESS);

            booking.checkOut();

            assertEquals(Booking.Status.CLOSED, booking.getStatus());
            assertNotNull(booking.getCheckedOutAt());
        }

        @Test
        @DisplayName("should throw InvalidBookingStateException when status is not IN_PROGRESS")
        void shouldThrowInvalidBookingStateExceptionWhenStatusIsNotInProgress() {
            Booking booking = new Booking();
            booking.setStatus(Booking.Status.CREATED);

            assertThrows(InvalidBookingStateException.class, booking::checkOut);
        }
    }

    @DisplayName("getPrice method")
    @Test
    void getPrice() {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.of(2021, 1, 1, 10, 0));
        booking.setEndTime(LocalDateTime.of(2021, 1, 1, 15, 0));

        booking.setPricingStrategy(new HourlyPricingStrategy(20));
        assertEquals(100, booking.getPrice());
    }

}