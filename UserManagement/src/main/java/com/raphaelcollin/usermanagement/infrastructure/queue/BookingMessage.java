package com.raphaelcollin.usermanagement.infrastructure.queue;

public record BookingMessage(String userId, String bookingId) {
}
