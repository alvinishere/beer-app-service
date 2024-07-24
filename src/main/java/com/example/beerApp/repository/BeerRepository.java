package com.example.beerApp.repository;

import com.example.beerApp.entity.AuthUser;
import com.example.beerApp.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Optional<Beer> findByName(String name);

    List<Beer> findByAuthUsers(AuthUser authUser);
}
