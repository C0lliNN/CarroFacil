package com.raphaelcollin.bookingservice.infrastructure.persistence;

import com.raphaelcollin.bookingservice.IntegrationTest;
import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyFactory;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JDBCBookingRepositoryTest extends IntegrationTest {
    @Autowired
    private JDBCBookingRepository jdbcBookingRepository;

    @Nested
    @DisplayName("save(Booking)")
    class SaveMethod {

        @Test
        @DisplayName("When booking is not saved, then it should be saved and return the saved booking")
        void shouldSaveABooking() {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.IN_PROGRESS);
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setStartTime(LocalDateTime.now());
            booking.setEndTime(LocalDateTime.now().plusHours(1));
            booking.setCreatedAt(LocalDateTime.now());

            Booking savedBooking = jdbcBookingRepository.save(booking);

            assertNotNull(savedBooking.getId());
            assertEquals(booking.getUserId(), savedBooking.getUserId());
            assertEquals(booking.getVehicleId(), savedBooking.getVehicleId());
            assertEquals(booking.getStatus(), savedBooking.getStatus());
            assertEquals(booking.getPricingStrategy(), savedBooking.getPricingStrategy());
            assertEquals(booking.getStartTime(), savedBooking.getStartTime());
            assertEquals(booking.getEndTime(), savedBooking.getEndTime());
            assertEquals(booking.getCreatedAt(), savedBooking.getCreatedAt());
        }

        @Test
        @DisplayName("When booking is saved, then it should be updated and return the updated booking")
        void shouldUpdateABooking() {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.IN_PROGRESS);
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setStartTime(LocalDateTime.now());
            booking.setEndTime(LocalDateTime.now().plusHours(1));
            booking.setCreatedAt(LocalDateTime.now());

            Booking savedBooking = jdbcBookingRepository.save(booking);

            savedBooking.setStatus(Booking.Status.CLOSED);
            savedBooking.setCheckedOutAt(LocalDateTime.now());

            Booking updatedBooking = jdbcBookingRepository.save(savedBooking);

            assertEquals(savedBooking.getId(), updatedBooking.getId());
            assertEquals(savedBooking.getUserId(), updatedBooking.getUserId());
            assertEquals(savedBooking.getVehicleId(), updatedBooking.getVehicleId());
            assertEquals(savedBooking.getStatus(), updatedBooking.getStatus());
            assertEquals(savedBooking.getPricingStrategy(), updatedBooking.getPricingStrategy());
            assertEquals(savedBooking.getStartTime(), updatedBooking.getStartTime());
            assertEquals(savedBooking.getEndTime(), updatedBooking.getEndTime());
            assertEquals(savedBooking.getCreatedAt(), updatedBooking.getCreatedAt());
            assertEquals(savedBooking.getCheckedOutAt(), updatedBooking.getCheckedOutAt());
        }
    }

    @Nested
    @DisplayName("findById(int)")
    class FindByIdMethod {

        @Test
        @DisplayName("When booking is found, then it should return the booking")
        void shouldReturnABooking() {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.IN_PROGRESS);
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setStartTime(LocalDateTime.now());
            booking.setEndTime(LocalDateTime.now().plusHours(1));
            booking.setCreatedAt(LocalDateTime.now());

            Booking savedBooking = jdbcBookingRepository.save(booking);

            Booking foundBooking = jdbcBookingRepository.findById(savedBooking.getId()).orElseThrow();

            assertEquals(savedBooking.getId(), foundBooking.getId());
            assertEquals(savedBooking.getUserId(), foundBooking.getUserId());
            assertEquals(savedBooking.getVehicleId(), foundBooking.getVehicleId());
            assertEquals(savedBooking.getStatus(), foundBooking.getStatus());
            assertEquals(savedBooking.getPricingStrategy().getType(), foundBooking.getPricingStrategy().getType());
        }

        @Test
        @DisplayName("When booking is not found, then it should return an empty optional")
        void shouldReturnAnEmptyOptional() {
            assertTrue(jdbcBookingRepository.findById(0).isEmpty());
        }
    }

    @Nested
    @DisplayName("findByUserId(String)")
    class FindByUserIdMethod {

        @Test
        @DisplayName("When bookings are found, then it should return the bookings")
        void shouldReturnBookings() {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.IN_PROGRESS);
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setStartTime(LocalDateTime.now());
            booking.setEndTime(LocalDateTime.now().plusHours(1));
            booking.setCreatedAt(LocalDateTime.now());

            Booking savedBooking = jdbcBookingRepository.save(booking);

            Booking booking2 = new Booking();
            booking2.setUserId("user-id");
            booking2.setVehicleId(2);
            booking2.setStatus(Booking.Status.IN_PROGRESS);
            booking2.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking2.setStartTime(LocalDateTime.now());
            booking2.setEndTime(LocalDateTime.now().plusHours(1));
            booking2.setCreatedAt(LocalDateTime.now());

            Booking savedBooking2 = jdbcBookingRepository.save(booking2);

            assertEquals(2, jdbcBookingRepository.findByUserId("user-id").size());
            assertEquals(savedBooking.getId(), jdbcBookingRepository.findByUserId("user-id").get(0).getId());
            assertEquals(savedBooking2.getId(), jdbcBookingRepository.findByUserId("user-id").get(1).getId());
        }

        @Test
        @DisplayName("When bookings are not found, then it should return an empty list")
        void shouldReturnAnEmptyList() {
            assertTrue(jdbcBookingRepository.findByUserId("user-id").isEmpty());
        }
    }

}