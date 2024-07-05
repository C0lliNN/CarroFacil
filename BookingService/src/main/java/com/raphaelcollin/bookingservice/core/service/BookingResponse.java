package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;

import java.time.LocalDateTime;

public record BookingResponse(
        int id,
        String userId,
        VehicleResponse vehicle,
        String status,
        double price,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime checkedInAt,
        LocalDateTime checkedOutAt
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                VehicleResponse.fromVehicle(booking.getVehicle()),
                booking.getStatus().name(),
                booking.getPrice(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getCreatedAt(),
                booking.getCheckedInAt(),
                booking.getCheckedOutAt()
        );
    }
}
