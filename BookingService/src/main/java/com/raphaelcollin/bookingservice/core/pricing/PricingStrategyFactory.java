package com.raphaelcollin.bookingservice.core.pricing;

public class PricingStrategyFactory {
    private static final double HOURLY_RATE = 10.0;

    public static BookingPricingStrategy createPricingStrategy(PricingStrategyType strategy) {
        return switch (strategy) {
            case HOURLY -> new HourlyPricingStrategy(HOURLY_RATE);
        };
    }
}
