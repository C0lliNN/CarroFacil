package com.raphaelcollin.inventorymanagement.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Store {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private long latitude;
    private long longitude;
}
