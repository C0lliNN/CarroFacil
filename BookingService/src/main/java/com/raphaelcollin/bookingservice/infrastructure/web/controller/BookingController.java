package com.raphaelcollin.bookingservice.infrastructure.web.controller;

import com.raphaelcollin.bookingservice.core.exception.AuthorizationException;
import com.raphaelcollin.bookingservice.core.service.BookingRequest;
import com.raphaelcollin.bookingservice.core.service.BookingResponse;
import com.raphaelcollin.bookingservice.core.service.BookingService;
import com.raphaelcollin.bookingservice.infrastructure.web.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookingRequest bookingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        bookingRequest = bookingRequest.withUserId(user.id());

        return ResponseEntity.status(201).body(bookingService.createBooking(bookingRequest));
    }

    @GetMapping("/bookings")
    public List<BookingResponse> getBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return bookingService.getBookingsByUserId(user.id());
    }

    @GetMapping("/bookings/{id}")
    public BookingResponse getBookingById(@PathVariable int id) {
        BookingResponse booking = bookingService.getBookingById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (Objects.equals(user.type(), "CUSTOMER") && !booking.userId().equals(user.id())) {
            throw new AuthorizationException("The booking does not belong to the user.");
        }

        return booking;
    }

    @PatchMapping("/bookings/{id}/check-in")
    public void checkIn(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (!Objects.equals(user.type(), "EMPLOYEE")) {
            throw new AuthorizationException("Only employees can check-in bookings.");
        }

        bookingService.checkin(id);
    }

    @PatchMapping("/bookings/{id}/check-out")
    public void checkOut(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (!Objects.equals(user.type(), "EMPLOYEE")) {
            throw new AuthorizationException("Only employees can check-out bookings.");
        }

        bookingService.checkout(id);
    }
}
