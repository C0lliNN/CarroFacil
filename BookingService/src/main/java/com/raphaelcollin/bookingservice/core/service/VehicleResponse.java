package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Vehicle;

public record VehicleResponse(
        int id,
        String make,
        String model,
        int year
) {
    public static VehicleResponse fromVehicle(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear());
    }
}
