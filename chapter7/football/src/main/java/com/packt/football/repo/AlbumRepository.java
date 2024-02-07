package com.packt.football.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Integer> {
    
    @Query("SELECT p FROM PlayerEntity p WHERE p NOT IN (SELECT c.player FROM CardEntity c WHERE c.album.id=:id)")
    public List<PlayerEntity> findByIdMissingPlayers(Integer id);

    @Query("SELECT p FROM PlayerEntity p JOIN p.cards c WHERE c.album.id = :id")
    public List<PlayerEntity> findByIdPlayers(Integer id, Pageable page);

    @Query("SELECT p FROM PlayerEntity p JOIN p.cards c WHERE c.album.id = :id AND p.team.id = :teamId")
    public List<PlayerEntity> findByIdAndTeam(Integer id, Integer teamId);

    public List<AlbumEntity> findAllByOwner(UserEntity user);
}
