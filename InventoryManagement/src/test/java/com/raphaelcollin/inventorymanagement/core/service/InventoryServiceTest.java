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

    @Mock
    private StoreRepository storeRepository;

    @Nested
    @DisplayName("method: saveStore(SaveStoreRequest)")
    class SaveStoreMethod {

        @Test
        @DisplayName("when called, then it should save the store and return a StoreResponse")
        void whenCalled_thenItShouldSaveTheStoreAndReturnAStoreResponse() {
            SaveStoreRequest request = new SaveStoreRequest(1, "Store 1", "Address 1", "2342342", 24, 88);
            when(storeRepository.save(Mockito.any())).thenReturn(request.toStore());

            StoreResponse response = inventoryService.saveStore(request);

            assertEquals(StoreResponse.fromStore(request.toStore()), response);
            verify(storeRepository).save(Mockito.any());
        }
    }

    @Nested
    @DisplayName("method: getStores()")
    class GetStoresMethod {

        @Test
        @DisplayName("when called, then it should return a list of StoreResponse")
        void whenCalled_thenItShouldReturnAListOfStoreResponse() {
            Store store1 = new SaveStoreRequest(1, "Store 1", "Address 1", "2342342", 24, 88).toStore();
            Store store2 = new SaveStoreRequest(2, "Store 2", "Address 2", "2342342", 24, 88).toStore();
            when(storeRepository.findAll()).thenReturn(List.of(store1, store2));

            StoreResponse response1 = StoreResponse.fromStore(store1);
            StoreResponse response2 = StoreResponse.fromStore(store2);

            List<StoreResponse> responses = inventoryService.getStores();

            assertEquals(List.of(response1, response2), responses);
            verify(storeRepository).findAll();
        }
    }

    @Nested
    @DisplayName("method: getVehicleTypesByStore(int)")
    class GetVehicleTypesByStoreMethod {

        @Test
        @DisplayName("when called, then it should return a list of VehicleTypeResponse")
        void whenCalled_thenItShouldReturnAListOfVehicleTypeResponse() {
            VehicleType type1 = new VehicleType(1, "Hyundai", "HB20", 2021, VehicleCategory.HATCH);
            VehicleType type2 = new VehicleType(2, "Chevrolet", "Onix", 2021, VehicleCategory.HATCH);
            when(vehicleRepository.getVehicleTypesByStore(1)).thenReturn(List.of(type1, type2));

            VehicleTypeResponse response1 = VehicleTypeResponse.fromVehicleType(type1);
            VehicleTypeResponse response2 = VehicleTypeResponse.fromVehicleType(type2);

            List<VehicleTypeResponse> responses = inventoryService.getVehicleTypesByStore(1);

            assertEquals(List.of(response1, response2), responses);
            verify(vehicleRepository).getVehicleTypesByStore(1);
        }
    }

    @Nested
    @DisplayName("method: saveVehicleType(SaveVehicleTypeRequest)")
    class SaveVehicleTypeMethod {

        @Test
        @DisplayName("when called, then it should save the vehicle type and return a VehicleTypeResponse")
        void whenCalled_thenItShouldSaveTheVehicleTypeAndReturnAVehicleTypeResponse() {
            SaveVehicleTypeRequest request = new SaveVehicleTypeRequest(1, "Hyundai", "HB20", 2021, VehicleCategory.HATCH);
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
            VehicleType type = new VehicleType(1, "Hyundai", "HB20", 2021, VehicleCategory.HATCH);
            Store store = new Store(1, "Store 1", "Address 1", "2342342", 24, 88);
            Vehicle vehicle = new Vehicle(1, type, store, 0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(vehicleRepository.getVehiclesByType(1)).thenReturn(List.of(vehicle));

            VehicleResponse response = VehicleResponse.fromVehicle(vehicle);

            List<VehicleResponse> responses = inventoryService.getVehiclesByType(1);

            assertEquals(List.of(response), responses);
            verify(vehicleRepository).getVehiclesByType(1);
        }
    }

    @Nested
    @DisplayName("method: saveVehicle(SaveVehicleRequest)")
    class SaveVehicleMethod {

        @Test
        @DisplayName("when store is not found, then it should throw an EntityNotFoundException")
        void whenStoreIsNotFound_thenItShouldThrowAnEntityNotFoundException() {
            SaveVehicleRequest request = new SaveVehicleRequest(1, 1, 1, 0, "ABC1234", "123456", "654321", "Black");
            when(storeRepository.findById(1)).thenReturn(java.util.Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> inventoryService.saveVehicle(request));
            verify(storeRepository).findById(1);
            verify(vehicleRepository, never()).findVehicleTypeById(1);
            verify(vehicleRepository, never()).saveVehicle(Mockito.any());
        }

        @Test
        @DisplayName("when vehicle type is not found, then it should throw an EntityNotFoundException")
        void whenVehicleTypeIsNotFound_thenItShouldThrowAnEntityNotFoundException() {
            SaveVehicleRequest request = new SaveVehicleRequest(1, 1, 1, 0, "ABC1234", "123456", "654321", "Black");
            Store store = new Store(1, "Store 1", "Address 1", "2342342", 24, 88);
            when(storeRepository.findById(1)).thenReturn(java.util.Optional.of(store));
            when(vehicleRepository.findVehicleTypeById(1)).thenReturn(java.util.Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> inventoryService.saveVehicle(request));
            verify(storeRepository).findById(1);
            verify(vehicleRepository).findVehicleTypeById(1);
            verify(vehicleRepository, never()).saveVehicle(Mockito.any());
        }

        @Test
        @DisplayName("when called, then it should save the vehicle and return a VehicleResponse")
        void whenCalled_thenItShouldSaveTheVehicleAndReturnAVehicleResponse() {
            SaveVehicleRequest request = new SaveVehicleRequest(1, 1, 1, 0, "ABC1234", "123456", "654321", "Black");
            Store store = new Store(1, "Store 1", "Address 1", "2342342", 24, 88);
            VehicleType type = new VehicleType(1, "Hyundai", "HB20", 2021, VehicleCategory.HATCH);
            Vehicle vehicle = new Vehicle(1, type, store, 0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(storeRepository.findById(1)).thenReturn(java.util.Optional.of(store));
            when(vehicleRepository.findVehicleTypeById(1)).thenReturn(java.util.Optional.of(type));
            when(vehicleRepository.saveVehicle(Mockito.any())).thenReturn(vehicle);

            VehicleResponse response = inventoryService.saveVehicle(request);

            assertEquals(VehicleResponse.fromVehicle(vehicle), response);
            verify(storeRepository).findById(1);
            verify(vehicleRepository).findVehicleTypeById(1);
            verify(vehicleRepository).saveVehicle(Mockito.any());
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
            Vehicle vehicle = new Vehicle(1, new VehicleType(1, "Hyundai", "HB20", 2021, VehicleCategory.HATCH), new Store(1, "Store 1", "Address 1", "2342342", 24, 88), 0, "ABC1234", "123456", "654321", "Black", VehicleStatus.AVAILABLE);
            when(vehicleRepository.findById(1)).thenReturn(java.util.Optional.of(vehicle));

            inventoryService.bookVehicle(1);

            assertEquals(VehicleStatus.BOOKED, vehicle.getStatus());
            verify(vehicleRepository).findById(1);
            verify(vehicleRepository).saveVehicle(vehicle);
        }
    }
}