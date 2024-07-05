package com.raphaelcollin.bookingservice.infrastructure.client;

import com.raphaelcollin.bookingservice.core.Vehicle;

public record VehicleResponse(
        int id,
        String make,
        String model,
        int year
) {
    public Vehicle toVehicle() {
        return new Vehicle(id, make, model, year);
    }
}
