package com.cinema.reservation;

import com.cinema.reservation.model.entity.Film;
import com.cinema.reservation.model.entity.Place;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTests {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @Test
    public void EveryoneAbleToReadFilms() throws Exception {
        mvc.perform(get("/films")).andExpect(status().isOk());
    }

    @Test
    public void EveryoneNotAbleToWriteFilms() throws Exception {
        mvc.perform(post("/films")).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    public void WriterAbleToWriteFilms() throws Exception {
        Film film = new Film();
        film.setTitle("test");
        film.setDescription("test");
        film.setGenre("test");

        mvc.perform(post("/films").with(
                jwt()
                .authorities(new SimpleGrantedAuthority("SCOPE_films:write")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonMapper.shared().writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    public void ReadsAllFilms() throws Exception {
        mvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    AtomicInteger counter = new AtomicInteger();

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
        concurrent(2, () -> {
            Place place = new Place();
            place.setId(7L);
            place.setFilmEventId(1L);
            place.setRow(2L);
            place.setColumn(3L);
            place.setPrice(250L);
            List<Place> places = List.of(place);
            try {
                MvcResult res = mvc.perform(post("/places")
                                .with(jwt().jwt(token -> token.subject("alice"))
                                        .authorities(new SimpleGrantedAuthority("SCOPE_reservations:write")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonMapper.shared().writeValueAsString(places)))
                        .andReturn();
                if (res.getResponse().getStatus() == HttpServletResponse.SC_OK) {
                    counter.addAndGet(1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(1, counter.get());
    }
}
