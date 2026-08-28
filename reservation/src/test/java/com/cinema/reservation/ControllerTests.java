package com.cinema.reservation;

import com.cinema.reservation.kafka.NotificationProducer;
import com.cinema.reservation.model.dto.PlaceDto;
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

    @MockitoBean
    NotificationProducer notificationProducer;

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
                .andExpect(status().isCreated());
    }

    @Test
    public void ReadsExactlyAllFilms() throws Exception {
        mvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }
}
