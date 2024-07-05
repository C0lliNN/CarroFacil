package com.raphaelcollin.inventorymanagement.infrastructure.web;

import com.raphaelcollin.inventorymanagement.core.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class InventoryManagementController {
    private final InventoryService service;

    @PutMapping("/stores")
    public StoreResponse saveStore(@RequestBody @Valid SaveStoreRequest request) {
        return service.saveStore(request);
    }

    @GetMapping("/stores")
    public List<StoreResponse> getStores() {
        return service.getStores();
    }

    @GetMapping("/stores/{storeId}/vehicle-types")
    public List<VehicleTypeResponse> getVehicleTypesByStore(@PathVariable int storeId) {
        return service.getVehicleTypesByStore(storeId);
    }

    @PutMapping("/vehicle-types")
    public VehicleTypeResponse saveVehicleType(@RequestBody @Valid SaveVehicleTypeRequest request) {
        return service.saveVehicleType(request);
    }

    @GetMapping("/vehicle-types/{typeId}/vehicles")
    public List<VehicleResponse> getVehiclesByType(@PathVariable int typeId) {
        return service.getVehiclesByType(typeId);
    }

    @GetMapping("/vehicles/{id}")
    public VehicleResponse getVehicleById(@PathVariable int id) {
        return service.getVehicleById(id);
    }

    @PutMapping("/vehicles")
    public VehicleResponse saveVehicle(@RequestBody @Valid SaveVehicleRequest request) {
        return service.saveVehicle(request);
    }

    @PatchMapping("/vehicles/{vehicleId}/book")
    public void bookVehicle(@PathVariable int vehicleId) {
        service.bookVehicle(vehicleId);
    }
}
