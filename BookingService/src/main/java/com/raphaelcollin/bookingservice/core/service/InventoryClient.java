package com.raphaelcollin.bookingservice.core.service;

import com.raphaelcollin.bookingservice.core.Vehicle;

public interface InventoryClient {
    Vehicle getVehicle(int id);
    void book(int vehicleId);
}
