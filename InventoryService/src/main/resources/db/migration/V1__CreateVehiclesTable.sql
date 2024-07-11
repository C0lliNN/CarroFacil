CREATE TABLE vehicle_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL
);

CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    type_id INT NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    mileage INT NOT NULL,
    license_plate VARCHAR(50) NOT NULL,
    chassis_number VARCHAR(50) NOT NULL,
    engine_number VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (type_id) REFERENCES vehicle_types (id) ON DELETE CASCADE
);
