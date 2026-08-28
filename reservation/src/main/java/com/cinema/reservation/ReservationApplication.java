package com.cinema.reservation;

import com.cinema.reservation.controller.EventController;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class ReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }

    @Bean
    NewTopic notifications() {
        return TopicBuilder.name("notifications").partitions(2).replicas(1).build();
    }
}
