package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.VehicleCategory;
import com.raphaelcollin.inventorymanagement.core.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SaveVehicleTypeRequest(
        @Min(value = 1, message = "Id must be a positive number")
        int id,

        String make,
        String model,
        int year,

        @NotBlank(message = "Category is required")
        VehicleCategory category
) {
    public VehicleType toVehicleType() {
        return new VehicleType(id, make, model, year, category);
    }
}
