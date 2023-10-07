package com.packt.footballpg;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;


public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer>{
    List<PlayerEntity> findByDateOfBirth(LocalDate dateOfBirth);
    List<PlayerEntity> findByNameContaining(String name);    
}
