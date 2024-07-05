package com.raphaelcollin.bookingservice.core.pricing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyFactoryTest {

    @Test
    void createPricingStrategy() {
        PricingStrategyType strategy = PricingStrategyType.HOURLY;
        BookingPricingStrategy bookingPricingStrategy = PricingStrategyFactory.createPricingStrategy(strategy);
        assertEquals(PricingStrategyType.HOURLY, bookingPricingStrategy.getType());
    }

}