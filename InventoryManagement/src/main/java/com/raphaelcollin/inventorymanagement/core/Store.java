package com.raphaelcollin.inventorymanagement.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Store {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private long latitude;
    private long longitude;
}
