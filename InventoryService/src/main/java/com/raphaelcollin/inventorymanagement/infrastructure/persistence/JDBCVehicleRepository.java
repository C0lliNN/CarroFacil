package com.raphaelcollin.inventorymanagement.infrastructure.persistence;


import com.raphaelcollin.inventorymanagement.core.*;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JDBCVehicleRepository implements VehicleRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_TYPES_QUERY = "INSERT INTO vehicle_types (name, category) VALUES (?, ?)";
    private static final String UPDATE_TYPES_QUERY = "UPDATE vehicle_types SET name = ?, category = ? WHERE id = ?";
    private static final String FIND_VEHICLE_TYPE_BY_ID_QUERY = "SELECT * FROM vehicle_types WHERE id = ?";

    private final RowMapper<VehicleType> rowMapper = (rs, rowNum) -> new VehicleType(
            rs.getInt("id"),
            rs.getString("name"),
            VehicleCategory.valueOf(rs.getString("category"))
    );

    private static final String INSERT_VEHICLE_QUERY = "INSERT INTO vehicles (type_id, make, model, year, mileage, license_plate, chassis_number, engine_number, color, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_VEHICLE_QUERY = "UPDATE vehicles SET type_id = ?,  make = ?, model = ?, year = ?, mileage = ?, license_plate = ?, chassis_number = ?, engine_number = ?, color = ?, status = ? WHERE id = ?";
    private static final String FIND_VEHICLE_BY_ID_QUERY = "SELECT v.id, v.make, v.model, v.year, v.mileage, v.license_plate, v.chassis_number, v.engine_number, v.color, v.status, vt.id as vt_id, vt.name as vt_name, vt.category as vt_category FROM vehicles v JOIN vehicle_types vt ON v.type_id = vt.id WHERE v.id = ?";
    private static final String FIND_VEHICLES_BY_TYPE_QUERY = "SELECT v.id, v.make, v.model, v.year, v.mileage, v.license_plate, v.chassis_number, v.engine_number, v.color, v.status, vt.id as vt_id, vt.name as vt_name, vt.category as vt_category FROM vehicles v JOIN vehicle_types vt ON v.type_id = vt.id WHERE v.type_id = ?";

    private final RowMapper<Vehicle> vehicleRowMapper = (rs, rowNum) -> {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getInt("id"));
        vehicle.setMake(rs.getString("make"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setYear(rs.getInt("year"));
        vehicle.setMileage(rs.getInt("mileage"));
        vehicle.setLicensePlate(rs.getString("license_plate"));
        vehicle.setChassisNumber(rs.getString("chassis_number"));
        vehicle.setEngineNumber(rs.getString("engine_number"));
        vehicle.setColor(rs.getString("color"));
        vehicle.setStatus(VehicleStatus.valueOf(rs.getString("status")));

        VehicleType vehicleType = new VehicleType(rs.getInt("vt_id"), rs.getString("vt_name"), VehicleCategory.valueOf(rs.getString("vt_category")));
        vehicle.setType(vehicleType);

        return vehicle;
    };

    @Override
    public Optional<VehicleType> findVehicleTypeById(int id) {
        List<VehicleType> vehicleTypes = jdbcTemplate.query(FIND_VEHICLE_TYPE_BY_ID_QUERY, new Object[]{id}, rowMapper);

        if (vehicleTypes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(vehicleTypes.get(0));
    }

    @Override
    public VehicleType saveVehicleType(VehicleType vehicleType) {
        if (vehicleType.getId() != 0) {
            jdbcTemplate.update(UPDATE_TYPES_QUERY, vehicleType.getName(), vehicleType.getCategory().name(), vehicleType.getId());
            return vehicleType;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_TYPES_QUERY, new String[]{"id"});
            ps.setString(1, vehicleType.getName());
            ps.setString(2, vehicleType.getCategory().name());
            return ps;
        }, keyHolder);

        vehicleType.setId(keyHolder.getKey().intValue());

        return vehicleType;
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        List<Vehicle> vehicles = jdbcTemplate.query(FIND_VEHICLE_BY_ID_QUERY, new Object[]{id}, vehicleRowMapper);

        if (vehicles.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(vehicles.get(0));
    }

    @Override
    public List<Vehicle> findVehiclesByType(int typeId) {
        return jdbcTemplate.query(FIND_VEHICLES_BY_TYPE_QUERY, new Object[]{typeId}, vehicleRowMapper);
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        if (vehicle.getId() != 0) {
            jdbcTemplate.update(
                    UPDATE_VEHICLE_QUERY,
                    vehicle.getType().getId(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getMileage(),
                    vehicle.getLicensePlate(),
                    vehicle.getChassisNumber(),
                    vehicle.getEngineNumber(),
                    vehicle.getColor(),
                    vehicle.getStatus().name(),
                    vehicle.getId()
            );
            return vehicle;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_VEHICLE_QUERY, new String[]{"id"});
            ps.setInt(1, vehicle.getType().getId());
            ps.setString(2, vehicle.getMake());
            ps.setString(3, vehicle.getModel());
            ps.setInt(4, vehicle.getYear());
            ps.setInt(5, vehicle.getMileage());
            ps.setString(6, vehicle.getLicensePlate());
            ps.setString(7, vehicle.getChassisNumber());
            ps.setString(8, vehicle.getEngineNumber());
            ps.setString(9, vehicle.getColor());
            ps.setString(10, vehicle.getStatus().name());
            return ps;

        }, keyHolder);

        vehicle.setId(keyHolder.getKey().intValue());

        return vehicle;
    }
}
