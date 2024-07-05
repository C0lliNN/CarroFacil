package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.BookingEvent;
import com.raphaelcollin.bookingservice.core.Vehicle;
import com.raphaelcollin.bookingservice.core.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {
    private BookingRepository bookingRepository;
    private InventoryClient inventoryClient;
    private BookingEventPublisher bookingEventPublisher;

    public List<BookingResponse> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId).stream()
                .peek(booking -> {
                    Vehicle vehicle = inventoryClient.getVehicle(booking.getVehicleId());
                    booking.setVehicle(vehicle);
                })
                .map(BookingResponse::from)
                .toList();
    }

    public BookingResponse getBookingById(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        Vehicle vehicle = inventoryClient.getVehicle(booking.getVehicleId());
        booking.setVehicle(vehicle);

        return BookingResponse.from(booking);
    }

    public BookingResponse createBooking(BookingRequest bookingRequest) {
        Booking booking = bookingRequest.createBooking();
        Vehicle vehicle = inventoryClient.getVehicle(booking.getVehicleId());
        booking.setVehicle(vehicle);

        booking = bookingRepository.save(booking);

        bookingEventPublisher.publishBookingEvent(
                new BookingEvent(booking.getId(), booking.getUserId(), vehicle.getId())
        );

        return BookingResponse.from(booking);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void checkin(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        booking.checkIn();
        bookingRepository.save(booking);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void checkout(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        booking.checkOut();
        bookingRepository.save(booking);
    }
}
