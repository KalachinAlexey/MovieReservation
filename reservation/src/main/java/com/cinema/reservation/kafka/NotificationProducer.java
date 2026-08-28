package com.cinema.reservation.kafka;

import com.cinema.reservation.model.entity.Reservation;

public interface NotificationProducer {
    void send(Reservation reservation);
}
