package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.VehicleCategory;
import com.raphaelcollin.inventorymanagement.core.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveVehicleTypeRequest(
        int id,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Category is required")
        VehicleCategory category
) {
    public VehicleType toVehicleType() {
        return new VehicleType(id, name, category);
    }
}
