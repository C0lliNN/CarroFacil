package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.Store;
import com.raphaelcollin.inventorymanagement.core.Vehicle;
import com.raphaelcollin.inventorymanagement.core.VehicleStatus;
import com.raphaelcollin.inventorymanagement.core.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SaveVehicleRequest(
        @Min(value = 1, message = "Id must be a positive number")
        int id,

        @Min(value = 1, message = "Type id must be a positive number")
        int typeId,

        @Min(value = 1, message = "Store id must be a positive number")
        int storeId,

        @NotBlank(message = "Make is required")
        String make,

        @NotBlank(message = "Model is required")
        String model,

        @Min(value = 1900, message = "Year must be greater than or equal to 1900")
        int year,

        @Min(value = 0, message = "Mileage must be a positive number")
        int mileage,

        @NotBlank(message = "License plate is required")
        String licensePlate,

        @NotBlank(message = "Chassis number is required")
        String chassisNumber,

        @NotBlank(message = "Engine number is required")
        String engineNumber,

        @NotBlank(message = "Color is required")
        String color
) {
    public Vehicle toVehicle(Store store, VehicleType type) {
        return new Vehicle(id, type, store, make, model, year, mileage, licensePlate, chassisNumber, engineNumber, color, VehicleStatus.AVAILABLE);
    }

}
