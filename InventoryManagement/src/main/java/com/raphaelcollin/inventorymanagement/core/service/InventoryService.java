package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.*;
import com.raphaelcollin.inventorymanagement.core.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {
    private final StoreRepository storeRepository;
    private final VehicleRepository vehicleRepository;

    public StoreResponse saveStore(SaveStoreRequest request) {
        return StoreResponse.fromStore(storeRepository.save(request.toStore()));
    }

    public List<StoreResponse> getStores() {
        return storeRepository.findAll().stream().map(StoreResponse::fromStore).toList();
    }

    public List<VehicleTypeResponse> getVehicleTypesByStore(int storeId) {
        return vehicleRepository.getVehicleTypesByStore(storeId).stream()
                .map(VehicleTypeResponse::fromVehicleType).toList();
    }

    public VehicleTypeResponse saveVehicleType(SaveVehicleTypeRequest request) {
        return VehicleTypeResponse.fromVehicleType(vehicleRepository.saveVehicleType(request.toVehicleType()));
    }

    public List<VehicleResponse> getVehiclesByType(int typeId) {
        return vehicleRepository.getVehiclesByType(typeId).stream()
                .map(VehicleResponse::fromVehicle).toList();
    }

    public VehicleResponse saveVehicle(SaveVehicleRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        VehicleType type = vehicleRepository.findVehicleTypeById(request.typeId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle type not found"));

        return VehicleResponse.fromVehicle(vehicleRepository.saveVehicle(request.toVehicle(store, type)));
    }

    @Transactional
    public void bookVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        vehicle.book();

        vehicleRepository.saveVehicle(vehicle);
    }
}
