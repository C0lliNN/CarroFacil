package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.*;
import com.raphaelcollin.inventorymanagement.core.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {
    private final VehicleRepository vehicleRepository;

    public VehicleTypeResponse saveVehicleType(SaveVehicleTypeRequest request) {
        return VehicleTypeResponse.fromVehicleType(vehicleRepository.saveVehicleType(request.toVehicleType()));
    }

    public List<VehicleResponse> getVehiclesByType(int typeId) {
        return vehicleRepository.findVehiclesByType(typeId).stream()
                .map(VehicleResponse::fromVehicle).toList();
    }

    public VehicleResponse getVehicleById(int id) {
        return VehicleResponse.fromVehicle(vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found")));
    }

    public VehicleResponse saveVehicle(SaveVehicleRequest request) {
        VehicleType type = vehicleRepository.findVehicleTypeById(request.typeId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle type not found"));

        return VehicleResponse.fromVehicle(vehicleRepository.saveVehicle(request.toVehicle(type)));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void bookVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        vehicle.book();

        vehicleRepository.saveVehicle(vehicle);
    }
}
