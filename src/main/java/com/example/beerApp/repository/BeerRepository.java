package com.example.beerApp.repository;

import com.example.beerApp.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    List<Beer> findAllByUsername(String username);
    Optional<Beer>findByName(String name);
}
