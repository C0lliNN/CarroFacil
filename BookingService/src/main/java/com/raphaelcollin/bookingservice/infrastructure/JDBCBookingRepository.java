package com.raphaelcollin.bookingservice.infrastructure;

import com.raphaelcollin.bookingservice.core.Booking;
import com.raphaelcollin.bookingservice.core.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class JDBCBookingRepository implements BookingRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Booking> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM booking WHERE id = ?", new Object[]{id}, (rs, rowNum) -> {
            Booking booking = new Booking();
            booking.setId(rs.getInt("id"));
            booking.setUserId(rs.getInt("user_id"));
            booking.setStartDate(rs.getDate("start_date").toLocalDate());
            booking.setEndDate(rs.getDate("end_date").toLocalDate());
            return booking;
        }).stream().findFirst();
    }
}
