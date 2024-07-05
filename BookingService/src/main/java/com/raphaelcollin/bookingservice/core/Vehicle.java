package com.raphaelcollin.bookingservice.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vehicle {
    private int id;
    private String make;
    private String model;
    private int year;
}
