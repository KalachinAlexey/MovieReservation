package com.cinema.reservation;

import com.cinema.reservation.kafka.NotificationProducer;
import com.cinema.reservation.model.dto.PlaceDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTests {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    NotificationProducer notificationProducer;

    private void concurrent(int n, Runnable consumer) {
        try (ExecutorService service = Executors.newFixedThreadPool(n)) {
            CountDownLatch latch = new CountDownLatch(n);
            for (int i = 0; i < n; i++) {
                service.submit(() -> {
                    try {
                        latch.countDown();
                        latch.await();
                        consumer.run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    @Test
    @DirtiesContext
    public void ParallelBookingRequests() throws Exception {
        AtomicInteger ok = new AtomicInteger();
        AtomicInteger conflict = new AtomicInteger();
        concurrent(2, () -> {
            PlaceDto place = new PlaceDto(7L, 1L, 2L, 3L);
            List<PlaceDto> places = List.of(place);
            try {
                MvcResult res = mvc.perform(post("/places")
                                .with(jwt().jwt(token -> token.subject("alice"))
                                        .authorities(new SimpleGrantedAuthority("SCOPE_reservations:write")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonMapper.shared().writeValueAsString(places)))
                        .andReturn();
                if (res.getResponse().getStatus() == HttpServletResponse.SC_OK) {
                    ok.addAndGet(1);
                }
                if (res.getResponse().getStatus() == HttpServletResponse.SC_CONFLICT) {
                    conflict.addAndGet(1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(1, ok.get());
        assertEquals(1, conflict.get());
    }
}
