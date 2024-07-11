package com.raphaelcollin.inventorymanagement.core;

import com.raphaelcollin.inventorymanagement.core.exception.VehicleAlreadyBookedException;
import com.raphaelcollin.inventorymanagement.core.exception.VehicleNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Nested
    @DisplayName("method: book")
    class BookMethod {

        @Test
        @DisplayName("when vehicle is booked, then throw VehicleAlreadyBookedException")
        void whenVehicleIsBooked_thenThrowVehicleAlreadyBookedException() {
            Vehicle vehicle = new Vehicle();
            vehicle.setStatus(VehicleStatus.BOOKED);

            assertThrows(VehicleAlreadyBookedException.class, vehicle::book);
        }

        @Test
        @DisplayName("when vehicle is unavailable, then throw VehicleNotAvailableException")
        void whenVehicleIsUnavailable_thenThrowVehicleNotAvailableException() {
            Vehicle vehicle = new Vehicle();
            vehicle.setStatus(VehicleStatus.UNAVAILABLE);

            assertThrows(VehicleNotAvailableException.class, vehicle::book);
        }

        @Test
        @DisplayName("when vehicle is available, then set status to BOOKED")
        void whenVehicleIsAvailable_thenSetStatusToBooked() {
            Vehicle vehicle = new Vehicle();
            vehicle.setStatus(VehicleStatus.AVAILABLE);

            vehicle.book();

            assertEquals(VehicleStatus.BOOKED, vehicle.getStatus());
        }
    }
}