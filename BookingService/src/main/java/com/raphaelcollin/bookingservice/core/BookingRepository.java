package com.raphaelcollin.bookingservice.core;

import java.util.Optional;

public interface BookingRepository {
    Optional<Booking> findById(int id);
}
