package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.Film;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FilmRepository extends CrudRepository<Film, Long> {
    List<Film> findAll();
    Film findByTitle(String title);
    Film findById(long id);
}
