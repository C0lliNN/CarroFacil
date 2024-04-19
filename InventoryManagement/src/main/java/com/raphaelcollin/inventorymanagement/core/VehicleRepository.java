package com.raphaelcollin.inventorymanagement.core;

import java.util.Optional;

public interface VehicleRepository {
    Optional<Vehicle> findById(int id);
    Vehicle save(Vehicle vehicle);
}
