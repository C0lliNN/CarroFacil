package com.raphaelcollin.bookingservice.core.pricing;

import com.raphaelcollin.bookingservice.core.Booking;

public interface BookingPricingStrategy {
    double calculatePrice(Booking booking);

    PricingStrategyType getType();
}
