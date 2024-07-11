package com.raphaelcollin.inventorymanagement.core.service;

import com.raphaelcollin.inventorymanagement.core.*;
import com.raphaelcollin.inventorymanagement.core.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Nested
    @DisplayName("method: saveVehicleType(SaveVehicleTypeRequest)")
    class SaveVehicleTypeMethod {

        @Test
        @DisplayName("when called, then it should save the vehicle type and return a VehicleTypeResponse")
        void whenCalled_thenItShouldSaveTheVehicleTypeAndReturnAVehicleTypeResponse() {
            SaveVehicleTypeRequest request = new SaveVehicleTypeRequest(1, "Hyundai", VehicleCategory.HATCH);
            when(vehicleRepository.saveVehicleType(Mockito.any())).thenReturn(request.toVehicleType());

            VehicleTypeResponse response = inventoryService.saveVehicleType(request);

            assertEquals(VehicleTypeResponse.fromVehicleType(request.toVehicleType()), response);
            verify(vehicleRepository).saveVehicleType(Mockito.any());
        }
    }

    @Nested
    @DisplayName("method: getVehiclesByType(VehicleType)")
    class GetVehiclesByTypeMethod {

        @Test
        @DisplayName("when called, then it should return a list of VehicleResponse")
        void whenCalled_thenItShouldReturnAListOfVehicleResponse() {
            VehicleType type = new VehicleType(1, "Hyundai", VehicleCategory.HATCH);
            Vehicle vehicle = new Vehicle(1, type, "Hyundai", "HB20", 2020, 0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(vehicleRepository.findVehiclesByType(1)).thenReturn(List.of(vehicle));

            VehicleResponse response = VehicleResponse.fromVehicle(vehicle);

            List<VehicleResponse> responses = inventoryService.getVehiclesByType(1);

            assertEquals(List.of(response), responses);
            verify(vehicleRepository).findVehiclesByType(1);
        }
    }

    @Nested
    @DisplayName("method: saveVehicle(SaveVehicleRequest)")
    class SaveVehicleMethod {

        @Test
        @DisplayName("when vehicle type is not found, then it should throw an EntityNotFoundException")
        void whenVehicleTypeIsNotFound_thenItShouldThrowAnEntityNotFoundException() {
            SaveVehicleRequest request = new SaveVehicleRequest(1, 1, "Hyundai", "HB20", 2020,0, "ABC1234", "123456", "654321", "Black");
            when(vehicleRepository.findVehicleTypeById(1)).thenReturn(java.util.Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> inventoryService.saveVehicle(request));
            verify(vehicleRepository).findVehicleTypeById(1);
            verify(vehicleRepository, never()).saveVehicle(Mockito.any());
        }

        @Test
        @DisplayName("when called, then it should save the vehicle and return a VehicleResponse")
        void whenCalled_thenItShouldSaveTheVehicleAndReturnAVehicleResponse() {
            SaveVehicleRequest request = new SaveVehicleRequest(1, 1, "Hyundai", "HB20", 2021, 0, "ABC1234", "123456", "654321", "Black");
            VehicleType type = new VehicleType(1, "Compact Hatch", VehicleCategory.HATCH);
            Vehicle vehicle = new Vehicle(1, type, "Hyundai", "HB20", 2021, 0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(vehicleRepository.findVehicleTypeById(1)).thenReturn(java.util.Optional.of(type));
            when(vehicleRepository.saveVehicle(Mockito.any())).thenReturn(vehicle);

            VehicleResponse response = inventoryService.saveVehicle(request);

            assertEquals(VehicleResponse.fromVehicle(vehicle), response);
            verify(vehicleRepository).findVehicleTypeById(1);
            verify(vehicleRepository).saveVehicle(Mockito.any());
        }
    }

    @Nested
    @DisplayName("method: getVehicleById(int)")
    class GetVehicleByIdMethod {

        @Test
        @DisplayName("when vehicle is not found, then it should throw an EntityNotFoundException")
        void whenVehicleIsNotFound_thenItShouldThrowAnEntityNotFoundException() {
            when(vehicleRepository.findById(1)).thenReturn(java.util.Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> inventoryService.getVehicleById(1));
            verify(vehicleRepository).findById(1);
        }

        @Test
        @DisplayName("when called, then it should return a VehicleResponse")
        void whenCalled_thenItShouldReturnAVehicleResponse() {
            VehicleType type = new VehicleType(1, "Compact Hatch", VehicleCategory.HATCH);
            Vehicle vehicle = new Vehicle(1, type, "Hyundai", "HB20", 2021, 0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(vehicleRepository.findById(1)).thenReturn(java.util.Optional.of(vehicle));

            VehicleResponse response = inventoryService.getVehicleById(1);

            assertEquals(VehicleResponse.fromVehicle(vehicle), response);
            verify(vehicleRepository).findById(1);
        }
    }

    @Nested
    @DisplayName("method: bookVehicle(int)")
    class BookVehicleMethod {

        @Test
        @DisplayName("when vehicle is not found, then it should throw an EntityNotFoundException")
        void whenVehicleIsNotFound_thenItShouldThrowAnEntityNotFoundException() {
            when(vehicleRepository.findById(1)).thenReturn(java.util.Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> inventoryService.bookVehicle(1));
            verify(vehicleRepository).findById(1);
            verify(vehicleRepository, never()).saveVehicle(Mockito.any());
        }

        @Test
        @DisplayName("when called, then it should book the vehicle")
        void whenCalled_thenItShouldBookTheVehicle() {
            VehicleType type = new VehicleType(1, "Compact Hatch", VehicleCategory.HATCH);

            Vehicle vehicle = new Vehicle(1, type, "Hyundai", "HB20", 2021,  0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(vehicleRepository.findById(1)).thenReturn(java.util.Optional.of(vehicle));

            inventoryService.bookVehicle(1);

            assertEquals(VehicleStatus.BOOKED, vehicle.getStatus());
            verify(vehicleRepository).findById(1);
            verify(vehicleRepository).saveVehicle(vehicle);
        }
    }
}