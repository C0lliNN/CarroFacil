package com.raphaelcollin.inventorymanagement.infrastructure;


import com.raphaelcollin.inventorymanagement.core.Vehicle;
import com.raphaelcollin.inventorymanagement.core.VehicleRepository;
import com.raphaelcollin.inventorymanagement.core.VehicleType;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JDBCVehicleRepository implements VehicleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Vehicle> findById(int id) {
//        List<Vehicle> vehicles = jdbcTemplate.query("SELECT * FROM vehicle WHERE id = ? LIMIT 1", new Object[]{id}, (rs, rowNum) -> {
//            Vehicle vehicle = new Vehicle();
//            vehicle.setId(rs.getInt("id"));
//            vehicle.setMake(rs.getString("make"));
//            vehicle.setModel(rs.getString("model"));
//            return vehicle;
//        });
//
//        if (vehicles.isEmpty()) {
//            return Optional.empty();
//        }
//
//        return Optional.of(vehicles.get(0));
        return Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
//        int id = jdbcTemplate.update("INSERT INTO vehicle (make, model) VALUES (?, ?)", vehicle.getMake(), vehicle.getModel());
//        vehicle.setId(id);
//
//        return vehicle;
        return null;
    }

    @Override
    public Optional<VehicleType> findVehicleTypeById(int id) {
        return Optional.empty();
    }

    @Override
    public List<VehicleType> getVehicleTypesByStore(int storeId) {
        return List.of();
    }

    @Override
    public VehicleType saveVehicleType(VehicleType vehicleType) {
        return null;
    }

    @Override
    public List<Vehicle> getVehiclesByType(int typeId) {
        return List.of();
    }

    @Override
    public Vehicle saveVehicle(Vehicle request) {
        return null;
    }
}
