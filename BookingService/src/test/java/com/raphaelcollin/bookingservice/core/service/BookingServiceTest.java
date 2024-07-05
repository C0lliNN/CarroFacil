package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.BookingEvent;
import com.raphaelcollin.bookingservice.core.Vehicle;
import com.raphaelcollin.bookingservice.core.exception.EntityNotFoundException;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyFactory;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private BookingEventPublisher bookingEventPublisher;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking(
                1,
                "user1",
                1,
                new Vehicle(1, "brand1", "model1", 2021),
                Booking.Status.CREATED,
                PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY),
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2021, 1, 1, 2, 0),
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2021, 1, 1, 0, 1),
                LocalDateTime.of(2021, 1, 1, 2, 2)
        );
    }

    @Nested
    @DisplayName("getBookingById method")
    class GetBookingById {

        @Test
        @DisplayName("when booking is not found, then it should throw a EntityNotFoundException")
        void whenBookingIsNotFound_thenItShouldThrowAEntityNotFoundException() {
            assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1));
        }

        @Test
        @DisplayName("when inventory client returns an error, then it should propagate the error")
        void whenInventoryClientReturnsAnError_thenItShouldPropagateTheError() {
            when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
            when(inventoryClient.getVehicle(1)).thenThrow(new RuntimeException("Error"));

            assertThrows(RuntimeException.class, () -> bookingService.getBookingById(1));
        }

        @Test
        @DisplayName("when booking is found and inventory client returns a vehicle, then it should return a BookResponse")
        void whenBookingIsFoundAndInventoryClientReturnsAVehicle_thenItShouldReturnABookResponse() {
            when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

            Vehicle vehicle = new Vehicle(1, "brand2", "model2", 2021);
            when(inventoryClient.getVehicle(1)).thenReturn(vehicle);

            BookingResponse actual = bookingService.getBookingById(1);
            BookingResponse expected = BookingResponse.from(booking);

            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("method: getBookingsByUserId")
    class GetBookingsByUserId {

        @Test
        @DisplayName("when inventory client returns an error, then it should propagate the error")
        void whenInventoryClientReturnsAnError_thenItShouldPropagateTheError() {
            when(bookingRepository.findByUserId("user1")).thenReturn(List.of(booking));
            when(inventoryClient.getVehicle(1)).thenThrow(new RuntimeException("Error"));

            assertThrows(RuntimeException.class, () -> bookingService.getBookingsByUserId("user1"));
        }

        @Test
        @DisplayName("when inventory client returns a vehicle, then it should return a list of BookResponse")
        void whenInventoryClientReturnsAVehicle_thenItShouldReturnAListOfBookResponse() {
            when(bookingRepository.findByUserId("user1")).thenReturn(List.of(booking));

            Vehicle vehicle = new Vehicle(1, "brand2", "model2", 2021);
            when(inventoryClient.getVehicle(1)).thenReturn(vehicle);

            List<BookingResponse> actual = bookingService.getBookingsByUserId("user1");
            List<BookingResponse> expected = List.of(BookingResponse.from(booking));

            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("method: createBooking")
    class CreateBooking {

        @Test
        @DisplayName("when inventory client returns an error, then it should propagate the error")
        void whenInventoryClientReturnsAnError_thenItShouldPropagateTheError() {
            BookingRequest bookingRequest = new BookingRequest(
                    1,
                    "user1",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    LocalDateTime.of(2021, 1, 1, 1, 0)
            );

            when(inventoryClient.getVehicle(1)).thenThrow(new RuntimeException("Error"));

            assertThrows(RuntimeException.class, () -> bookingService.createBooking(bookingRequest));
        }

        @Test
        @DisplayName("when event publisher returns an error, then it should propagate the error")
        void whenEventPublisherReturnsAnError_thenItShouldPropagateTheError() {
            BookingRequest bookingRequest = new BookingRequest(
                    1,
                    "user1",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    LocalDateTime.of(2021, 1, 1, 1, 0)
            );

            when(inventoryClient.getVehicle(1)).thenReturn(new Vehicle(1, "brand1", "model1", 2021));
            when(bookingRepository.save(booking)).thenReturn(booking);
            doThrow(new RuntimeException("Error")).when(bookingEventPublisher).publishBookingEvent(new BookingEvent(1, "user1", 1));

            assertThrows(RuntimeException.class, () -> bookingService.createBooking(bookingRequest));
        }

        @Test
        @DisplayName("when all operations are successful, then it should return a BookingResponse")
        void whenAllOperationsAreSuccessful_thenItShouldReturnABookingResponse() {
            BookingRequest bookingRequest = new BookingRequest(
                    1,
                    "user1",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    LocalDateTime.of(2021, 1, 1, 1, 0)
            );

            Vehicle vehicle = new Vehicle(1, "brand1", "model1", 2021);
            when(inventoryClient.getVehicle(1)).thenReturn(vehicle);

            booking.setVehicle(vehicle);
            when(bookingRepository.save(any())).thenReturn(booking);

            BookingResponse actual = bookingService.createBooking(bookingRequest);
            BookingResponse expected = BookingResponse.from(booking);

            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("method: checkin")
    class Checkin {

        @Test
        @DisplayName("when booking is not found, then it should throw a EntityNotFoundException")
        void whenBookingIsNotFound_thenItShouldThrowAEntityNotFoundException() {
            assertThrows(EntityNotFoundException.class, () -> bookingService.checkin(1));
        }

        @Test
        @DisplayName("when all operations are successful, then it should checkin the booking")
        void whenAllOperationsAreSuccessful_thenItShouldCheckinTheBooking() {
            when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
            when(bookingRepository.save(booking)).thenReturn(booking);

            bookingService.checkin(1);

            assertEquals(Booking.Status.IN_PROGRESS, booking.getStatus());
        }
    }

    @Nested
    @DisplayName("method: checkout")
    class Checkout {

        @Test
        @DisplayName("when booking is not found, then it should throw a EntityNotFoundException")
        void whenBookingIsNotFound_thenItShouldThrowAEntityNotFoundException() {
            assertThrows(EntityNotFoundException.class, () -> bookingService.checkout(1));
        }

        @Test
        @DisplayName("when all operations are successful, then it should checkout the booking")
        void whenAllOperationsAreSuccessful_thenItShouldCheckoutTheBooking() {
            booking.setStatus(Booking.Status.IN_PROGRESS);
            when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
            when(bookingRepository.save(booking)).thenReturn(booking);

            bookingService.checkout(1);

            assertEquals(Booking.Status.CLOSED, booking.getStatus());
        }
    }
}