CREATE SCHEMA IF NOT EXISTS reservation;

CREATE TABLE IF NOT EXISTS reservation.films (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50),
    description VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation.halls (
    id BIGINT PRIMARY KEY,
    rows BIGINT NOT NULL,
    columns BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation.events (
    id BIGSERIAL PRIMARY KEY,
    hall_id BIGINT NOT NULL REFERENCES reservation.halls(id) ON DELETE CASCADE,
    film_id BIGINT NOT NULL REFERENCES reservation.films(id) ON DELETE CASCADE,
    date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation.places (
    id BIGSERIAL PRIMARY KEY,
    film_event_id BIGINT NOT NULL REFERENCES reservation.events(id) ON DELETE CASCADE,
    row_number BIGINT NOT NULL,
    column_number BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL,
    reservation_id BIGINT REFERENCES reservation.reservations(id),
    price BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation.reservations (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50),
    total_price BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL
);