package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.BookingEvent;

public interface BookingEventPublisher {
    void publishBookingEvent(BookingEvent bookingEvent);
}
