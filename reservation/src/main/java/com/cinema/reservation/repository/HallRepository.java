package com.cinema.reservation.repository;

import com.cinema.reservation.model.entity.Hall;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HallRepository extends CrudRepository<Hall, Long> {
    List<Hall> findAll();
    Hall findHallById(Long id);
}
