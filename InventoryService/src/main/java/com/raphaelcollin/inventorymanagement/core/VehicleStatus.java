package com.raphaelcollin.inventorymanagement.core;

public enum VehicleStatus {
    AVAILABLE,
    UNAVAILABLE, // after checkout, the vehicle is unavailable for maintenance, etc..
    BOOKED,
}
