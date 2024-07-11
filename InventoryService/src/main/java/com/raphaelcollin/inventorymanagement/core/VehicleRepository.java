package com.raphaelcollin.inventorymanagement.core;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Optional<VehicleType> findVehicleTypeById(int id);
    List<VehicleType> getVehicleTypesByStore(int storeId);
    VehicleType saveVehicleType(VehicleType vehicleType);

    Optional<Vehicle> findById(int id);
    List<Vehicle> findVehiclesByType(int typeId);
    Vehicle saveVehicle(Vehicle request);
}
