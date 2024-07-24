package com.example.beerApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "auth_user")
public class AuthUser {
    @Id
    private UUID id;

    @JsonIgnore
    @ManyToMany(mappedBy = "authUsers")
//    @JoinTable(
//            name = "beer_user",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "beer_id")
//    )
    private Set<Beer> beers;
}
