package com.raphaelcollin.bookingservice.infrastructure.web.controller;

import com.raphaelcollin.bookingservice.IntegrationTest;
import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.Vehicle;
import com.raphaelcollin.bookingservice.core.exception.InventoryBookingException;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyFactory;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import com.raphaelcollin.bookingservice.core.service.BookingRepository;
import com.raphaelcollin.bookingservice.core.service.InventoryClient;
import com.raphaelcollin.bookingservice.security.WithMockCustomer;
import com.raphaelcollin.bookingservice.security.WithMockEmployee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest extends IntegrationTest {
    @MockBean
    InventoryClient inventoryClient;

    @Autowired
    private BookingRepository bookingRepository;

    @Nested
    @DisplayName("POST /bookings")
    class PostBookings {

        @Test
        @DisplayName("When request is not valid, then it should return 400 Bad Request")
        void whenRequestIsNotValid_shouldReturn400() throws Exception {
            mockMvc.perform(post("/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"vehicleId\": 1," +
                                    "\"userId\": \"\"," +
                                    "\"startTime\": \"2023-01-01T00:00:00\"," +
                                    "\"endTime\": \"2023-01-02T00:00:00\"" +
                                    "}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockEmployee
        @DisplayName("when vehicle is not available, then it should return 409 Conflict")
        void whenVehicleIsNotAvailable_shouldReturn409Conflict() throws Exception {
            when(inventoryClient.getVehicle(1)).thenThrow(new InventoryBookingException("Vehicle is not available."));

            mockMvc.perform(post("/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"vehicleId\": 1," +
                                    "\"userId\": \"user-id\"," +
                                    "\"startTime\": \"2026-01-01T00:00:00\"," +
                                    "\"endTime\": \"2026-01-02T00:00:00\"" +
                                    "}"))
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockEmployee
        @DisplayName("when request is valid, then it should return 201 Created")
        void whenRequestIsValid_shouldReturn201() throws Exception {
            when(inventoryClient.getVehicle(1)).thenReturn(new Vehicle(1, "vehicle", "model", 2023));

            mockMvc.perform(post("/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"vehicleId\": 1," +
                                    "\"userId\": \"user-id\"," +
                                    "\"startTime\": \"2026-01-01T00:00:00\"," +
                                    "\"endTime\": \"2026-01-02T00:00:00\"" +
                                    "}"))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("GET /bookings")
    class GetBookings {

        @Test
        @WithMockCustomer
        @DisplayName("When request is valid, then it should return 200 OK")
        void whenRequestIsValid_shouldReturn200() throws Exception {
            when(inventoryClient.getVehicle(1)).thenReturn(new Vehicle(1, "vehicle", "model", 2023));

            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.CREATED);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            bookingRepository.save(booking);

            booking.setId(0);
            booking.setUserId("some-id");
            booking = bookingRepository.save(booking);

            mockMvc.perform(get("/bookings"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(booking.getId()))
                    .andExpect(jsonPath("$[0].userId").value(booking.getUserId()))
                    .andExpect(jsonPath("$[0].status").value(booking.getStatus().name()));
        }
    }

    @Nested
    @DisplayName("GET /bookings/{id}")
    class GetBookingById {

        @Test
        @WithMockCustomer
        @DisplayName("When booking does not belong to user, then it should return 403 Forbidden")
        void whenBookingDoesNotBelongToUser_shouldReturn403() throws Exception {
            when(inventoryClient.getVehicle(1)).thenReturn(new Vehicle(1, "vehicle", "model", 2023));

            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.CREATED);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            mockMvc.perform(get("/bookings/" + booking.getId()))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockCustomer
        @DisplayName("When booking belongs to user, then it should return 200 OK")
        void whenBookingBelongsToUser_shouldReturn200() throws Exception {
            when(inventoryClient.getVehicle(1)).thenReturn(new Vehicle(1, "vehicle", "model", 2023));

            Booking booking = new Booking();
            booking.setUserId("some-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.CREATED);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            mockMvc.perform(get("/bookings/" + booking.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(booking.getId()))
                    .andExpect(jsonPath("$.userId").value(booking.getUserId()))
                    .andExpect(jsonPath("$.status").value(booking.getStatus().name()));

        }
    }

    @Nested
    @DisplayName("PATCH /bookings/{id}/check-in")
    class PatchCheckIn {

        @Test
        @WithMockCustomer
        @DisplayName("When user is not an employee, then it should return 403 Forbidden")
        void whenUserIsNotAnEmployee_shouldReturn403() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1/check-in"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockEmployee
        @DisplayName("When booking is not in CREATED state, then it should return 400 Bad Request")
        void whenBookingIsNotInCreatedState_shouldReturn400() throws Exception {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.IN_PROGRESS);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/" + booking.getId() + "/check-in"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockEmployee
        @DisplayName("When booking is in CREATED state, then it should return 204 No Content")
        void whenBookingIsInCreatedState_shouldReturn204() throws Exception {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.CREATED);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/" + booking.getId() + "/check-in"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PATCH /bookings/{id}/check-out")
    class PatchCheckOut {

        @Test
        @WithMockCustomer
        @DisplayName("When user is not an employee, then it should return 403 Forbidden")
        void whenUserIsNotAnEmployee_shouldReturn403() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1/check-out"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockEmployee
        @DisplayName("When booking is not in IN_PROGRESS state, then it should return 400 Bad Request")
        void whenBookingIsNotInProgress_shouldReturn400() throws Exception {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.CREATED);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/" + booking.getId() + "/check-out"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockEmployee
        @DisplayName("When booking is in IN_PROGRESS state, then it should return 204 No Content")
        void whenBookingIsInProgress_shouldReturn204() throws Exception {
            Booking booking = new Booking();
            booking.setUserId("user-id");
            booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.HOURLY));
            booking.setVehicleId(1);
            booking.setStatus(Booking.Status.IN_PROGRESS);
            booking.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setEndTime(LocalDateTime.of(2023, 1, 1, 0, 0));
            booking.setCreatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/" + booking.getId() + "/check-out"))
                    .andExpect(status().isOk());
        }
    }
}