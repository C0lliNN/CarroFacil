package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.Store;
import com.raphaelcollin.inventorymanagement.core.Vehicle;
import com.raphaelcollin.inventorymanagement.core.VehicleStatus;

public record VehicleResponse(
        int id,
        VehicleTypeResponse type,
        Store store,
        String make,
        String model,
        int year,
        int mileage,
        String licensePlate,
        String chassisNumber,
        String engineNumber,
        String color,
        VehicleStatus status
) {
    public static VehicleResponse fromVehicle(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                VehicleTypeResponse.fromVehicleType(vehicle.getType()),
                vehicle.getStore(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getMileage(),
                vehicle.getLicensePlate(),
                vehicle.getChassisNumber(),
                vehicle.getEngineNumber(),
                vehicle.getColor(),
                vehicle.getStatus()
        );
    }
}
