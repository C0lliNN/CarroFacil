package com.raphaelcollin.inventorymanagement.infrastructure.persistence;

import com.raphaelcollin.inventorymanagement.IntegrationTest;
import com.raphaelcollin.inventorymanagement.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCVehicleRepositoryTest extends IntegrationTest {

    @Autowired
    private JDBCVehicleRepository jdbcVehicleRepository;

    @Nested
    @DisplayName("saveVehicleType method")
    class SaveVehicleTypeMethod {

        @Test
        @DisplayName("when called and item is not persisted, then it should insert it")
        void whenCalled_thenItShouldSaveTheVehicleType() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);

            VehicleType savedVehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            assertNotEquals(0, savedVehicleType.getId());
            assertEquals(vehicleType.getName(), savedVehicleType.getName());
            assertEquals(vehicleType.getCategory(), savedVehicleType.getCategory());
            assertTrue(jdbcVehicleRepository.findVehicleTypeById(savedVehicleType.getId()).isPresent());
        }

        @Test
        @DisplayName("when called and item is persisted, then it should update it")
        void whenCalled_thenItShouldUpdateTheVehicleType() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            VehicleType savedVehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            savedVehicleType.setName("Updated Vehicle Type");
            savedVehicleType.setCategory(VehicleCategory.SUV);

            VehicleType updatedVehicleType = jdbcVehicleRepository.saveVehicleType(savedVehicleType);

            assertEquals(savedVehicleType.getId(), updatedVehicleType.getId());
            assertEquals(savedVehicleType.getName(), updatedVehicleType.getName());
            assertEquals(savedVehicleType.getCategory(), updatedVehicleType.getCategory());
        }
    }

    @Nested
    @DisplayName("findVehicleTypeById method")
    class FindVehicleTypeByIdMethod {

        @Test
        @DisplayName("when called with an existing id, then it should return the vehicle type")
        void whenCalledWithAnExistingId_thenItShouldReturnTheVehicleType() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            VehicleType savedVehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            assertTrue(jdbcVehicleRepository.findVehicleTypeById(savedVehicleType.getId()).isPresent());
        }

        @Test
        @DisplayName("when called with a non-existing id, then it should return an empty optional")
        void whenCalledWithANonExistingId_thenItShouldReturnAnEmptyOptional() {
            assertFalse(jdbcVehicleRepository.findVehicleTypeById(1).isPresent());
        }
    }


    @Nested
    @DisplayName("findById method")
    class FindByIdMethod {

        @Test
        @DisplayName("when called with an existing id, then it should return the vehicle")
        void whenCalledWithAnExistingId_thenItShouldReturnTheVehicle() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            Vehicle vehicle = new Vehicle(0, vehicleType, "Make", "Model", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            Vehicle savedVehicle = jdbcVehicleRepository.saveVehicle(vehicle);

            assertTrue(jdbcVehicleRepository.findById(savedVehicle.getId()).isPresent());
        }

        @Test
        @DisplayName("when called with a non-existing id, then it should return an empty optional")
        void whenCalledWithANonExistingId_thenItShouldReturnAnEmptyOptional() {
            assertFalse(jdbcVehicleRepository.findById(1).isPresent());
        }
    }


    @Nested
    @DisplayName("findVehiclesByType method")
    class FindVehiclesByTypeMethod {

        @Test
        @DisplayName("when called with an existing type id, then it should return all vehicles")
        void whenCalledWithAnExistingTypeId_thenItShouldReturnAllVehicles() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            VehicleType vehicleType2 = new VehicleType(0, "Vehicle Type 2", VehicleCategory.SUV);
            vehicleType2 = jdbcVehicleRepository.saveVehicleType(vehicleType2);

            Vehicle vehicle1 = new Vehicle(0, vehicleType, "Make 1", "Model 1", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            Vehicle vehicle2 = new Vehicle(0, vehicleType2, "Make 2", "Model 2", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);

            vehicle1 = jdbcVehicleRepository.saveVehicle(vehicle1);
           jdbcVehicleRepository.saveVehicle(vehicle2);

            List<Vehicle> vehicles = jdbcVehicleRepository.findVehiclesByType(vehicleType.getId());

            assertEquals(1, vehicles.size());
            assertEquals(vehicle1.getId(), vehicles.get(0).getId());
        }

        @Test
        @DisplayName("when called with a non-existing type id, then it should return an empty list")
        void whenCalledWithANonExistingTypeId_thenItShouldReturnAnEmptyList() {
            assertTrue(jdbcVehicleRepository.findVehiclesByType(1).isEmpty());
        }
    }

    @Nested
    @DisplayName("saveVehicle method")
    class SaveVehicleMethod {

        @Test
        @DisplayName("when called and item is not persisted, then it should insert it")
        void whenCalled_thenItShouldSaveTheVehicle() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            Vehicle vehicle = new Vehicle(0, vehicleType, "Make", "Model", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);

            Vehicle savedVehicle = jdbcVehicleRepository.saveVehicle(vehicle);

            assertNotEquals(0, savedVehicle.getId());
            assertEquals(vehicleType.getId(), savedVehicle.getType().getId());
            assertEquals(vehicle.getMake(), savedVehicle.getMake());
            assertEquals(vehicle.getModel(), savedVehicle.getModel());
            assertEquals(vehicle.getYear(), savedVehicle.getYear());
            assertEquals(vehicle.getMileage(), savedVehicle.getMileage());
            assertEquals(vehicle.getLicensePlate(), savedVehicle.getLicensePlate());
            assertEquals(vehicle.getChassisNumber(), savedVehicle.getChassisNumber());
            assertEquals(vehicle.getEngineNumber(), savedVehicle.getEngineNumber());
            assertEquals(vehicle.getColor(), savedVehicle.getColor());
            assertEquals(vehicle.getStatus(), savedVehicle.getStatus());
            assertTrue(jdbcVehicleRepository.findById(savedVehicle.getId()).isPresent());
        }

        @Test
        @DisplayName("when called and item is persisted, then it should update it")
        void whenCalled_thenItShouldUpdateTheVehicle() {
            VehicleType vehicleType = new VehicleType(0, "Vehicle Type", VehicleCategory.HATCH);
            vehicleType = jdbcVehicleRepository.saveVehicleType(vehicleType);

            Vehicle vehicle = new Vehicle(0, vehicleType, "Make", "Model", 2020, 0, "ABC1234", "123456", "123456", "Black", VehicleStatus.AVAILABLE);
            Vehicle savedVehicle = jdbcVehicleRepository.saveVehicle(vehicle);

            savedVehicle.setMake("Updated Make");
            savedVehicle.setModel("Updated Model");
            savedVehicle.setYear(2021);
            savedVehicle.setMileage(1000);
            savedVehicle.setLicensePlate("DEF5678");
            savedVehicle.setChassisNumber("654321");
            savedVehicle.setEngineNumber("654321");
            savedVehicle.setColor("White");
            savedVehicle.setStatus(VehicleStatus.UNAVAILABLE);

            Vehicle updatedVehicle = jdbcVehicleRepository.saveVehicle(savedVehicle);

            assertEquals(savedVehicle.getId(), updatedVehicle.getId());
            assertEquals(savedVehicle.getType().getId(), updatedVehicle.getType().getId());
            assertEquals(savedVehicle.getMake(), updatedVehicle.getMake());
            assertEquals(savedVehicle.getModel(), updatedVehicle.getModel());
            assertEquals(savedVehicle.getYear(), updatedVehicle.getYear());
            assertEquals(savedVehicle.getMileage(), updatedVehicle.getMileage());
            assertEquals(savedVehicle.getLicensePlate(), updatedVehicle.getLicensePlate());
            assertEquals(savedVehicle.getChassisNumber(), updatedVehicle.getChassisNumber());
            assertEquals(savedVehicle.getEngineNumber(), updatedVehicle.getEngineNumber());
            assertEquals(savedVehicle.getColor(), updatedVehicle.getColor());
            assertEquals(savedVehicle.getStatus(), updatedVehicle.getStatus());

        }
    }
}