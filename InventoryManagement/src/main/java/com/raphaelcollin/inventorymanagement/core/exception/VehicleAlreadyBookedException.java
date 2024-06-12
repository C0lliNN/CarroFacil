package com.raphaelcollin.inventorymanagement.core.exception;

public class VehicleAlreadyBookedException extends RuntimeException {
    public VehicleAlreadyBookedException(String message) {
        super(message);
    }
}
