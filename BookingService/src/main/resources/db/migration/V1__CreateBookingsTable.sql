CREATE TABLE bookings(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    vehicle_id INTEGER NOT NULL,
    pricing_strategy_id VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL,
    created_at timestamp NOT NULL,
    checked_in_at timestamp,
    checked_out_at timestamp
);