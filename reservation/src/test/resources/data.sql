
INSERT INTO reservation.films (id, title, description, genre) VALUES
    (1, 'Интерстеллар', 'Экспедиция отправляется сквозь червоточину на поиски нового дома для человечества', 'Фантастика'),
    (2, 'Достать ножи', 'Детектив расследует загадочную смерть главы большой и конфликтной семьи', 'Детектив'),
    (3, 'Унесённые призраками', 'Девочка попадает в мир духов и пытается спасти своих родителей', 'Анимация');

INSERT INTO reservation.halls (id, rows, columns) VALUES
    (1, 3, 4),
    (2, 4, 5);

INSERT INTO reservation.events (id, hall_id, film_id, date) VALUES
    (1, 1, 1, '2030-01-15 18:00:00'),
    (2, 1, 2, '2030-01-15 21:00:00'),
    (3, 2, 3, '2030-01-16 12:00:00'),
    (4, 2, 1, '2030-01-16 19:30:00');

INSERT INTO reservation.reservations (id, username, total_price, status) VALUES
    (1, 'alice', 400, 'RESERVED'),
    (2, 'bob', 590, 'PAID'),
    (3, 'charlie', 0, 'CANCELLED'),
    (4, 'alice', 330, 'RESERVED');

INSERT INTO reservation.places
    (id, film_event_id, row_number, column_number, status, reservation_id, price)
VALUES
    -- Event 1: hall 1 (3 x 4), including reserved, paid and blocked seats.
    (1,  1, 1, 1, 'BOOKED', 1,    200),
    (2,  1, 1, 2, 'BOOKED', 1,    200),
    (3,  1, 1, 3, 'EMPTY',  NULL, 200),
    (4,  1, 1, 4, 'BLOCKED', NULL, 200),
    (5,  1, 2, 1, 'EMPTY',  NULL, 250),
    (6,  1, 2, 2, 'BOOKED', 2,    250),
    (7,  1, 2, 3, 'EMPTY',  NULL, 250),
    (8,  1, 2, 4, 'EMPTY',  NULL, 250),
    (9,  1, 3, 1, 'EMPTY',  NULL, 300),
    (10, 1, 3, 2, 'EMPTY',  NULL, 300),
    (11, 1, 3, 3, 'EMPTY',  NULL, 300),
    (12, 1, 3, 4, 'BLOCKED', NULL, 300),

    -- Event 2: hall 1 (3 x 4), mostly empty for booking scenarios.
    (13, 2, 1, 1, 'EMPTY',  NULL, 220),
    (14, 2, 1, 2, 'EMPTY',  NULL, 220),
    (15, 2, 1, 3, 'EMPTY',  NULL, 220),
    (16, 2, 1, 4, 'EMPTY',  NULL, 220),
    (17, 2, 2, 1, 'EMPTY',  NULL, 270),
    (18, 2, 2, 2, 'EMPTY',  NULL, 270),
    (19, 2, 2, 3, 'EMPTY',  NULL, 270),
    (20, 2, 2, 4, 'BLOCKED', NULL, 270),
    (21, 2, 3, 1, 'EMPTY',  NULL, 320),
    (22, 2, 3, 2, 'EMPTY',  NULL, 320),
    (23, 2, 3, 3, 'EMPTY',  NULL, 320),
    (24, 2, 3, 4, 'EMPTY',  NULL, 320),

    -- Event 3: hall 2 (4 x 5), with seats belonging to a paid reservation.
    (25, 3, 1, 1, 'EMPTY',  NULL, 180),
    (26, 3, 1, 2, 'EMPTY',  NULL, 180),
    (27, 3, 1, 3, 'EMPTY',  NULL, 180),
    (28, 3, 1, 4, 'EMPTY',  NULL, 180),
    (29, 3, 1, 5, 'BLOCKED', NULL, 180),
    (30, 3, 2, 1, 'EMPTY',  NULL, 230),
    (31, 3, 2, 2, 'EMPTY',  NULL, 230),
    (32, 3, 2, 3, 'EMPTY',  NULL, 230),
    (33, 3, 2, 4, 'EMPTY',  NULL, 230),
    (34, 3, 2, 5, 'EMPTY',  NULL, 230),
    (35, 3, 3, 1, 'EMPTY',  NULL, 280),
    (36, 3, 3, 2, 'EMPTY',  NULL, 280),
    (37, 3, 3, 3, 'EMPTY',  NULL, 280),
    (38, 3, 3, 4, 'EMPTY',  NULL, 280),
    (39, 3, 3, 5, 'EMPTY',  NULL, 280),
    (40, 3, 4, 1, 'BOOKED', 2,    340),
    (41, 3, 4, 2, 'EMPTY',  NULL, 340),
    (42, 3, 4, 3, 'EMPTY',  NULL, 340),
    (43, 3, 4, 4, 'EMPTY',  NULL, 340),
    (44, 3, 4, 5, 'BLOCKED', NULL, 340),

    -- Event 4: hall 2 (4 x 5), including another reservation by alice.
    (45, 4, 1, 1, 'EMPTY',  NULL, 190),
    (46, 4, 1, 2, 'EMPTY',  NULL, 190),
    (47, 4, 1, 3, 'BLOCKED', NULL, 190),
    (48, 4, 1, 4, 'EMPTY',  NULL, 190),
    (49, 4, 1, 5, 'EMPTY',  NULL, 190),
    (50, 4, 2, 1, 'EMPTY',  NULL, 240),
    (51, 4, 2, 2, 'EMPTY',  NULL, 240),
    (52, 4, 2, 3, 'EMPTY',  NULL, 240),
    (53, 4, 2, 4, 'EMPTY',  NULL, 240),
    (54, 4, 2, 5, 'EMPTY',  NULL, 240),
    (55, 4, 3, 1, 'EMPTY',  NULL, 290),
    (56, 4, 3, 2, 'EMPTY',  NULL, 290),
    (57, 4, 3, 3, 'EMPTY',  NULL, 290),
    (58, 4, 3, 4, 'EMPTY',  NULL, 290),
    (59, 4, 3, 5, 'EMPTY',  NULL, 290),
    (60, 4, 4, 1, 'EMPTY',  NULL, 330),
    (61, 4, 4, 2, 'BOOKED', 4,    330),
    (62, 4, 4, 3, 'EMPTY',  NULL, 330),
    (63, 4, 4, 4, 'EMPTY',  NULL, 330),
    (64, 4, 4, 5, 'BLOCKED', NULL, 330);

-- Keep BIGSERIAL sequences above the explicitly inserted fixture IDs.
SELECT setval(pg_get_serial_sequence('reservation.films', 'id'), 3, true);
SELECT setval(pg_get_serial_sequence('reservation.events', 'id'), 4, true);
SELECT setval(pg_get_serial_sequence('reservation.reservations', 'id'), 4, true);
SELECT setval(pg_get_serial_sequence('reservation.places', 'id'), 64, true);
