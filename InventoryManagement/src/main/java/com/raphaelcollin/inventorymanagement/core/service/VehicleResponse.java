package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.Store;
import com.raphaelcollin.inventorymanagement.core.VehicleStatus;

public record VehicleResponse(
        int id,
        VehicleTypeResponse type,
        Store store,
        int mileage,
        String licensePlate,
        String chassisNumber,
        String engineNumber,
        String color,
        VehicleStatus status
) {
    public static VehicleResponse fromVehicle(com.raphaelcollin.inventorymanagement.core.Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                VehicleTypeResponse.fromVehicleType(vehicle.getType()),
                vehicle.getStore(),
                vehicle.getMileage(),
                vehicle.getLicensePlate(),
                vehicle.getChassisNumber(),
                vehicle.getEngineNumber(),
                vehicle.getColor(),
                vehicle.getStatus()
        );
    }
}
