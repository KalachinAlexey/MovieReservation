package com.cinema.notifications.controller;

import com.cinema.contracts.notifications.v1.ReservationNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);

    @KafkaListener(topics = "notifications", groupId = "default")
    public void consume (ReservationNotification message) {
        LOGGER.info("Message received: {}", message);
    }
}
