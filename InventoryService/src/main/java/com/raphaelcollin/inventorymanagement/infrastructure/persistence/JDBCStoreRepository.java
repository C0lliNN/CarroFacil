package com.raphaelcollin.inventorymanagement.infrastructure.persistence;

import com.raphaelcollin.inventorymanagement.core.Store;
import com.raphaelcollin.inventorymanagement.core.StoreRepository;
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
public class JDBCStoreRepository implements StoreRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_QUERY = "INSERT INTO stores (name, address, phone_number, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE stores SET name = ?, address = ?, phone_number = ?, latitude = ?, longitude = ? WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM stores";
    private static final String FIND_ONE_QUERY = "SELECT * FROM stores WHERE id = ?";

    private final RowMapper<Store> rowMapper = (rs, rowNum) -> new Store(rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getString("phone_number"), rs.getLong("latitude"), rs.getLong("longitude"));

    @Override
    public Store save(Store store) {
        if (store.getId() != 0) {
            jdbcTemplate.update(UPDATE_QUERY, store.getName(), store.getAddress(), store.getPhoneNumber(), store.getLatitude(), store.getLongitude(), store.getId());
            return store;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
            ps.setString(1, store.getName());
            ps.setString(2, store.getAddress());
            ps.setString(3, store.getPhoneNumber());
            ps.setLong(4, store.getLatitude());
            ps.setLong(5, store.getLongitude());
            return ps;
        }, keyHolder);

        store.setId(keyHolder.getKey().intValue());

        return store;
    }

    @Override
    public List<Store> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, rowMapper);
    }

    @Override
    public Optional<Store> findById(int id) {
        List<Store> stores = jdbcTemplate.query(FIND_ONE_QUERY, new Object[]{id}, rowMapper);
        if (stores.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(stores.get(0));
    }
}
