package com.raphaelcollin.inventorymanagement.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VehicleType {
    private int id;
    private String make;
    private String model;
    private int year;
    private VehicleCategory category;
}
