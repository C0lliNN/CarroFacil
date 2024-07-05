package com.raphaelcollin.bookingservice.core.exception;

public class InvalidBookingStateException extends IllegalArgumentException {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}
