package com.raphaelcollin.inventorymanagement.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VehicleType {
    private int id;
    private String name;
    private VehicleCategory category;
}
