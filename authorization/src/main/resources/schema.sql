CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE IF NOT EXISTS auth.users (
         id BIGSERIAL PRIMARY KEY,
         username VARCHAR(50) NOT NULL,
         password VARCHAR(100) NOT NULL,
         role VARCHAR(50) NOT NULL,
         disabled BOOLEAN NOT NULL
);
