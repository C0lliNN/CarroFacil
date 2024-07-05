package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Optional<Booking> findById(int id);
    List<Booking> findByUserId(String userId);
    Booking save(Booking booking);
}
