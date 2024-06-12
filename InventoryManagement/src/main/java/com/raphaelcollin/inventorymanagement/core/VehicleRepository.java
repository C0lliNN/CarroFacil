package com.raphaelcollin.inventorymanagement.core;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Optional<Vehicle> findById(int id);
    Vehicle save(Vehicle vehicle);

    Optional<VehicleType> findVehicleTypeById(int id);
    List<VehicleType> getVehicleTypesByStore(int storeId);
    VehicleType saveVehicleType(VehicleType vehicleType);

    List<Vehicle> getVehiclesByType(int typeId);
    Vehicle saveVehicle(Vehicle request);
}
