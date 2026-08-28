package com.cinema.reservation;

import com.cinema.reservation.kafka.NotificationProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReservationApplicationTests {

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    NotificationProducer notificationProducer;

    @Test
    void contextLoads() {
    }

}
