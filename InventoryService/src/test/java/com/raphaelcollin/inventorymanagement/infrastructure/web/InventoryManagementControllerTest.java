package com.raphaelcollin.inventorymanagement.infrastructure.web;

import com.raphaelcollin.inventorymanagement.IntegrationTest;
import com.raphaelcollin.inventorymanagement.core.*;
import com.raphaelcollin.inventorymanagement.core.service.SaveVehicleRequest;
import com.raphaelcollin.inventorymanagement.infrastructure.security.WithMockEmployee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InventoryManagementControllerTest extends IntegrationTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Nested
    @DisplayName("GET /stores")
    class GetStores {

        @Test
        @DisplayName("When user is authenticated, then it should return a list of stores")
        void whenUserIsAuthenticatedShouldReturnStores() throws Exception {
            Store store1 = new Store(0, "Name 1", "Address 1", "234242", 24, 46);
            Store store2 = new Store(0, "Name 2", "Address 2", "234243", 25, 47);

            store1 = storeRepository.save(store1);
            store2 = storeRepository.save(store2);

            mockMvc.perform(get("/stores").header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(store1.getId()))
                    .andExpect(jsonPath("$[0].name").value(store1.getName()))
                    .andExpect(jsonPath("$[0].address").value(store1.getAddress()))
                    .andExpect(jsonPath("$[0].latitude").value(store1.getLatitude()))
                    .andExpect(jsonPath("$[0].longitude").value(store1.getLongitude()))
                    .andExpect(jsonPath("$[1].id").value(store2.getId()))
                    .andExpect(jsonPath("$[1].name").value(store2.getName()))
                    .andExpect(jsonPath("$[1].address").value(store2.getAddress()))
                    .andExpect(jsonPath("$[1].latitude").value(store2.getLatitude()))
                    .andExpect(jsonPath("$[1].longitude").value(store2.getLongitude()));
        }
    }

    @Nested
    @DisplayName("PUT /stores")
    class PutStores {

        @Test
        @WithMockEmployee
        @DisplayName("when request is not valid, then it should return 400")
        void whenRequestIsNotValidShouldReturn400() throws Exception {
            mockMvc.perform(put("/stores").contentType("application/json").content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockEmployee
        @DisplayName("when request is valid, then it should return 200")
        void whenRequestIsValidShouldReturn200() throws Exception {
            mockMvc.perform(put("/stores").contentType("application/json")
                            .content("{\"name\":\"Name\",\"phoneNumber\":\"252342\",\"address\":\"Address\",\"latitude\":24,\"longitude\":46}"))
                    .andExpect(status().isOk());

            assertEquals(1, storeRepository.findAll().size());
        }
    }

    @Nested
    @DisplayName("PUT /vehicle-types")
    class PutVehicleTypes {

        @Test
        @WithMockEmployee
        @DisplayName("when request is not valid, then it should return 400")
        void whenRequestIsNotValidShouldReturn400() throws Exception {
            mockMvc.perform(put("/vehicle-types").contentType("application/json").content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockEmployee
        @DisplayName("when request is valid, then it should return 200")
        void whenRequestIsValidShouldReturn200() throws Exception {
            mockMvc.perform(put("/vehicle-types").contentType("application/json")
                            .content("{\"name\":\"Vehicle Type 1\",\"category\":\"HATCH\"}"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /stores/{storeId}/vehicle-types")
    class GetVehicleTypesByStore {

        @Test
        @DisplayName("when store does not have vehicle types, then it should return an empty list")
        void whenStoreDoesNotHaveVehicleTypesShouldReturnEmptyList() throws Exception {
            Store store = new Store(0, "Name", "Address", "234242", 24, 46);
            store = storeRepository.save(store);

            mockMvc.perform(get("/stores/" + store.getId() + "/vehicle-types").header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }


        @Test
        @DisplayName("when store exists, then it should return a list of vehicle types")
        void whenStoreExistsShouldReturnVehicleTypes() throws Exception {
            VehicleType vehicleType1 = new VehicleType(0, "Vehicle Type 1", VehicleCategory.HATCH);
            VehicleType vehicleType2 = new VehicleType(0, "Vehicle Type 2", VehicleCategory.SUV);
            VehicleType vehicleType3 = new VehicleType(0, "Vehicle Type 3", VehicleCategory.SEDAN);

            vehicleType1 = vehicleRepository.saveVehicleType(vehicleType1);
            vehicleType2 = vehicleRepository.saveVehicleType(vehicleType2);
            vehicleRepository.saveVehicleType(vehicleType3);

            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            store = storeRepository.save(store);

            Vehicle vehicle1 = new Vehicle(0, vehicleType1, store, "Make 1", "Model 1", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            Vehicle vehicle2 = new Vehicle(0, vehicleType2, store, "Make 2", "Model 2", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);

            vehicleRepository.saveVehicle(vehicle1);
            vehicleRepository.saveVehicle(vehicle2);

            mockMvc.perform(get("/stores/" + store.getId() + "/vehicle-types").header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(vehicleType1.getId()))
                    .andExpect(jsonPath("$[0].name").value(vehicleType1.getName()))
                    .andExpect(jsonPath("$[0].category").value(vehicleType1.getCategory().name()))
                    .andExpect(jsonPath("$[1].id").value(vehicleType2.getId()))
                    .andExpect(jsonPath("$[1].name").value(vehicleType2.getName()))
                    .andExpect(jsonPath("$[1].category").value(vehicleType2.getCategory().name()));
        }
    }

    @Nested
    @DisplayName("GET /vehicle-types/{typeId}/vehicles")
    class GetVehiclesByType {

        @Test
        @DisplayName("when vehicle type does not have vehicles, then it should return an empty list")
        void whenVehicleTypeDoesNotHaveVehiclesShouldReturnEmptyList() throws Exception {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = vehicleRepository.saveVehicleType(vehicleType);

            mockMvc.perform(get("/vehicle-types/" + vehicleType.getId() + "/vehicles").header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("when vehicle type exists, then it should return a list of vehicles")
        void whenVehicleTypeExistsShouldReturnVehicles() throws Exception {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = vehicleRepository.saveVehicleType(vehicleType);

            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            store = storeRepository.save(store);

            Vehicle vehicle1 = new Vehicle(0, vehicleType, store, "Make 1", "Model 1", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            Vehicle vehicle2 = new Vehicle(0, vehicleType, store, "Make 2", "Model 2", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);

            vehicleRepository.saveVehicle(vehicle1);
            vehicleRepository.saveVehicle(vehicle2);

            mockMvc.perform(get("/vehicle-types/" + vehicleType.getId() + "/vehicles").header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(vehicle1.getId()))
                    .andExpect(jsonPath("$[0].licensePlate").value(vehicle1.getLicensePlate()))
                    .andExpect(jsonPath("$[0].mileage").value(vehicle1.getMileage()))
                    .andExpect(jsonPath("$[0].chassisNumber").value(vehicle1.getChassisNumber()))
                    .andExpect(jsonPath("$[0].engineNumber").value(vehicle1.getEngineNumber()))
                    .andExpect(jsonPath("$[0].color").value(vehicle1.getColor()))
                    .andExpect(jsonPath("$[0].status").value(vehicle1.getStatus().name()))
                    .andExpect(jsonPath("$[1].id").value(vehicle2.getId()))
                    .andExpect(jsonPath("$[1].licensePlate").value(vehicle2.getLicensePlate()))
                    .andExpect(jsonPath("$[1].mileage").value(vehicle2.getMileage()))
                    .andExpect(jsonPath("$[1].chassisNumber").value(vehicle2.getChassisNumber()))
                    .andExpect(jsonPath("$[1].engineNumber").value(vehicle2.getEngineNumber()))
                    .andExpect(jsonPath("$[1].color").value(vehicle2.getColor()))
                    .andExpect(jsonPath("$[1].status").value(vehicle2.getStatus().name()));
        }
    }

    @Nested
    @DisplayName("PUT /vehicles")
    class PutVehicles {

        @Test
        @WithMockEmployee
        @DisplayName("when request is not valid, then it should return 400")
        void whenRequestIsNotValidShouldReturn400() throws Exception {
            mockMvc.perform(put("/vehicles").contentType("application/json").content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockEmployee
        @DisplayName("when request is valid, then it should return 200")
        void whenRequestIsValidShouldReturn200() throws Exception {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = vehicleRepository.saveVehicleType(vehicleType);

            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            store = storeRepository.save(store);

            SaveVehicleRequest request = new SaveVehicleRequest(
                    vehicleType.getId(),
                    store.getId(),
                    vehicleType.getId(),
                    "Make",
                    "Model",
                    2020,
                    0,
                    "ABC1234",
                    "123456",
                    "123456",
                    "Black"
            );

            mockMvc.perform(put("/vehicles").contentType("application/json")
                            .content("{\"typeId\":" + request.typeId() + ",\"storeId\":" + request.storeId() + ",\"make\":\"" + request.make() + "\",\"model\":\"" + request.model() + "\",\"year\":" + request.year() + ",\"mileage\":" + request.mileage() + ",\"licensePlate\":\"" + request.licensePlate() + "\",\"chassisNumber\":\"" + request.chassisNumber() + "\",\"engineNumber\":\"" + request.engineNumber() + "\",\"color\":\"" + request.color() + "\"}"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /vehicles/{id}")
    class GetVehicleById {

        @Test
        @DisplayName("when vehicle does not exist, then it should return 404")
        void whenVehicleDoesNotExistShouldReturn404() throws Exception {
            mockMvc.perform(get("/vehicles/1").header("Authorization", "Bearer valid"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("when vehicle exists, then it should return 200")
        void whenVehicleExistsShouldReturn200() throws Exception {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = vehicleRepository.saveVehicleType(vehicleType);

            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            store = storeRepository.save(store);

            Vehicle vehicle = new Vehicle(0, vehicleType, store, "Make", "Model", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            vehicle = vehicleRepository.saveVehicle(vehicle);

            mockMvc.perform(get("/vehicles/" + vehicle.getId()).header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(vehicle.getId()))
                    .andExpect(jsonPath("$.licensePlate").value(vehicle.getLicensePlate()))
                    .andExpect(jsonPath("$.mileage").value(vehicle.getMileage()))
                    .andExpect(jsonPath("$.chassisNumber").value(vehicle.getChassisNumber()))
                    .andExpect(jsonPath("$.engineNumber").value(vehicle.getEngineNumber()))
                    .andExpect(jsonPath("$.color").value(vehicle.getColor()))
                    .andExpect(jsonPath("$.status").value(vehicle.getStatus().name()));
        }
    }

    @Nested
    @DisplayName("PATCH /vehicles/{vehicleId}/book")
    class PatchVehiclesBook {

        @Test
        @DisplayName("when vehicle is not available, then it should return 400")
        void whenVehicleIsNotAvailableShouldReturn400() throws Exception {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = vehicleRepository.saveVehicleType(vehicleType);

            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            store = storeRepository.save(store);

            Vehicle vehicle = new Vehicle(0, vehicleType, store, "Make", "Model", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.UNAVAILABLE);
            vehicle = vehicleRepository.saveVehicle(vehicle);

            mockMvc.perform(patch("/vehicles/" + vehicle.getId() + "/book").header("Authorization", "Bearer valid"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("when vehicle is available, then it should return 204")
        void whenVehicleIsAvailableShouldReturn204() throws Exception {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = vehicleRepository.saveVehicleType(vehicleType);

            Store store = new Store(0, "Store", "Address", "123456789", 0L, 0L);
            store = storeRepository.save(store);

            Vehicle vehicle = new Vehicle(0, vehicleType, store, "Make", "Model", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            vehicle = vehicleRepository.saveVehicle(vehicle);

            mockMvc.perform(patch("/vehicles/" + vehicle.getId() + "/book").header("Authorization", "Bearer valid"))
                    .andExpect(status().isOk());
        }
    }
}