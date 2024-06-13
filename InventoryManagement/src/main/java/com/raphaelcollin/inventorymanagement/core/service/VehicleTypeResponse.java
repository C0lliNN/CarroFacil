package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.VehicleCategory;
import com.raphaelcollin.inventorymanagement.core.VehicleType;

public record VehicleTypeResponse(
        int id,
        String name,
        VehicleCategory category
) {
    public static VehicleTypeResponse fromVehicleType(VehicleType vehicleType) {
        return new VehicleTypeResponse(
                vehicleType.getId(),
                vehicleType.getName(),
                vehicleType.getCategory()
        );
    }
}
