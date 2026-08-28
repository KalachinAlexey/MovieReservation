package com.cinema.reservation.kafka;

import com.cinema.contracts.notifications.v1.ReservationNotification;
import com.cinema.contracts.notifications.v1.ReservationNotificationStatus;
import com.cinema.reservation.model.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer implements NotificationProducer {
    private final KafkaTemplate<String, ReservationNotification> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, ReservationNotification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Reservation reservation) {
        kafkaTemplate.send("notifications",
                new ReservationNotification(
                        reservation.getId(),
                        reservation.getUsername(),
                        reservation.getTotalPrice(),
                        ReservationNotificationStatus.valueOf(
                                reservation.getStatus().name()
                        )
                ));
    }
}
