package com.packt.football.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.ownedCards c LEFT JOIN FETCH u.ownedAlbums LEFT JOIN FETCH c.player WHERE u.id = ?1")
    public Optional<UserEntity> findByIdWithCardsAndAlbums(Integer id);

}
