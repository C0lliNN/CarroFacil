package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.VehicleCategory;

public record VehicleTypeResponse(
        int id,
        String make,
        String model,
        int year,
        VehicleCategory category
) {
    public static VehicleTypeResponse fromVehicleType(com.raphaelcollin.inventorymanagement.core.VehicleType vehicleType) {
        return new VehicleTypeResponse(
                vehicleType.getId(),
                vehicleType.getMake(),
                vehicleType.getModel(),
                vehicleType.getYear(),
                vehicleType.getCategory()
        );
    }
}
