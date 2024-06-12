package com.raphaelcollin.inventorymanagement.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class VehicleType {
    private int id;
    private String make;
    private String model;
    private int year;
    private VehicleCategory category;
}
