package com.raphaelcollin.inventorymanagement.infrastructure.web;

import com.raphaelcollin.inventorymanagement.core.exception.AuthorizationException;
import com.raphaelcollin.inventorymanagement.core.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class InventoryManagementController {
    private final InventoryService service;


    @PutMapping("/vehicle-types")
    public VehicleTypeResponse saveVehicleType(@RequestBody @Valid SaveVehicleTypeRequest request) {
        checkAuthorization();

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
        checkAuthorization();

        return service.saveVehicle(request);
    }

    @PatchMapping("/vehicles/{vehicleId}/book")
    public void bookVehicle(@PathVariable int vehicleId) {
        service.bookVehicle(vehicleId);
    }

    private void checkAuthorization() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.type().equals("EMPLOYEE")) {
            throw new AuthorizationException("Only Employees can perform this operation.");
        }
    }
}
