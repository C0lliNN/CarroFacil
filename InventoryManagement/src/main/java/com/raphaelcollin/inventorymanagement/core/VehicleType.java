package com.raphaelcollin.inventorymanagement.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VehicleType {
    private int id;
    private String name;
    private VehicleCategory category;
}
