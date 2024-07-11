package com.raphaelcollin.inventorymanagement.core;

import com.raphaelcollin.inventorymanagement.core.exception.VehicleAlreadyBookedException;
import com.raphaelcollin.inventorymanagement.core.exception.VehicleNotAvailableException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Vehicle {
    private int id;
    private VehicleType type;
    private Store store;
    private String make;
    private String model;
    private int year;
    private int mileage;
    private String licensePlate;
    private String chassisNumber;
    private String engineNumber;
    private String color;
    private VehicleStatus status;

    public void book() {
        if (status == VehicleStatus.BOOKED) {
            throw new VehicleAlreadyBookedException("Vehicle is not available for booking");
        }

        if (status == VehicleStatus.UNAVAILABLE) {
            throw new VehicleNotAvailableException("Vehicle is not available for booking");
        }

        status = VehicleStatus.BOOKED;
    }
}
