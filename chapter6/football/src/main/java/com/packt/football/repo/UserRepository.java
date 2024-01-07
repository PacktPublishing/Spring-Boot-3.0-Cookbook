package com.packt.football.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.ownedCards c JOIN FETCH u.ownedAlbums JOIN FETCH c.player WHERE u.id = ?1")
    public UserEntity findByIdWithCardsAndAlbums(Integer id);

}
