package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.Store;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SaveStoreRequest(
        int id,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @Min(value = 0, message = "Latitude must be a positive number")
        @Max(value = 90, message = "Latitude must be less than or equal to 90")
        long latitude,

        @Min(value = 0, message = "Longitude must be a positive number")
        @Max(value = 180, message = "Longitude must be less than or equal to 180")
        long longitude
) {
    public Store toStore() {
        return new Store(id, name, address, phoneNumber, latitude, longitude);
    }
}
