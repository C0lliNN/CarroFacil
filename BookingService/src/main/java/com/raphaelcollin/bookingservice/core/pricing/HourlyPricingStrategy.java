package com.raphaelcollin.bookingservice.core.pricing;

import com.raphaelcollin.bookingservice.core.Booking;

import static java.time.temporal.ChronoUnit.HOURS;

public class HourlyPricingStrategy implements BookingPricingStrategy {
    private final double hourlyRate;

    public HourlyPricingStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public PricingStrategyType getType() {
        return PricingStrategyType.HOURLY;
    }

    @Override
    public double calculatePrice(Booking booking) {
        long hours = booking.getStartTime().until(booking.getEndTime(), HOURS);
        return hours * hourlyRate;
    }
}
