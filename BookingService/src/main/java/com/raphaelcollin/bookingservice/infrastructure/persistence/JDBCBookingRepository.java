package com.raphaelcollin.bookingservice.infrastructure.persistence;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyFactory;
import com.raphaelcollin.bookingservice.core.pricing.PricingStrategyType;
import com.raphaelcollin.bookingservice.core.service.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class JDBCBookingRepository implements BookingRepository {
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BOOKING_BY_ID = "SELECT * FROM bookings WHERE id = ? LIMIT 1";
    private static final String SELECT_BOOKINGS_BY_USER_ID = "SELECT * FROM bookings WHERE user_id = ?";
    private static final String INSERT_BOOKING = "INSERT INTO bookings (user_id, vehicle_id, status, pricing_strategy_id, start_time, end_time, created_at, checked_in_at, checked_out_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOKING = "UPDATE bookings SET status = ?, checked_in_at = ?, checked_out_at = ? WHERE id = ?";

    private final RowMapper<Booking> bookingRowMapper = (rs, rowNum) -> {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getString("user_id"));
        booking.setVehicleId(rs.getInt("vehicle_id"));
        booking.setStatus(Booking.Status.valueOf(rs.getString("status")));
        booking.setPricingStrategy(PricingStrategyFactory.createPricingStrategy(PricingStrategyType.valueOf(rs.getString("pricing_strategy_id"))));
        booking.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        booking.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        booking.setCheckedInAt(Optional.ofNullable(rs.getTimestamp("checked_in_at")).map(Timestamp::toLocalDateTime).orElse(null));
        booking.setCheckedOutAt(Optional.ofNullable(rs.getTimestamp("checked_out_at")).map(Timestamp::toLocalDateTime).orElse(null));

        return booking;
    };

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() != 0) {
            jdbcTemplate.update(UPDATE_BOOKING, booking.getStatus().name(), booking.getCheckedInAt(), booking.getCheckedOutAt(), booking.getId());
            return booking;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_BOOKING, new String[]{"id"});
            ps.setString(1, booking.getUserId());
            ps.setInt(2, booking.getVehicleId());
            ps.setString(3, booking.getStatus().name());
            ps.setString(4, booking.getPricingStrategy().getType().name());
            ps.setTimestamp(5, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(booking.getEndTime()));
            ps.setTimestamp(7, Timestamp.valueOf(booking.getCreatedAt()));
            ps.setTimestamp(8, Optional.ofNullable(booking.getCheckedInAt()).map(Timestamp::valueOf).orElse(null));
            ps.setTimestamp(9, Optional.ofNullable(booking.getCheckedOutAt()).map(Timestamp::valueOf).orElse(null));
            return ps;
        }, keyHolder);

        booking.setId(keyHolder.getKey().intValue());
        return booking;
    }

    @Override
    public Optional<Booking> findById(int id) {
        return jdbcTemplate.query(SELECT_BOOKING_BY_ID, new Object[]{id}, bookingRowMapper).stream().findFirst();
    }

    @Override
    public List<Booking> findByUserId(String userId) {
        return jdbcTemplate.query(SELECT_BOOKINGS_BY_USER_ID, new Object[]{userId}, bookingRowMapper);
    }
}
