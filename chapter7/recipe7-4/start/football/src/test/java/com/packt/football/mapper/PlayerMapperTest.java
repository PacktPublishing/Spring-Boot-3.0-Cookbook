package com.packt.football.mapper;

import org.junit.jupiter.api.Test;
import com.packt.football.domain.Player;
import com.packt.football.repo.PlayerEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    @Test
    void map() {
        // Given
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(377762);
        playerEntity.setName("Aitana BONMATI");
        playerEntity.setJerseyNumber(6);
        playerEntity.setPosition("Midfielder");
        playerEntity.setDateOfBirth(LocalDate.of(1998, 1, 18));

        // When
        PlayerMapper playerMapper = new PlayerMapper();
        Player player = playerMapper.map(playerEntity);

        // Then
        assertEquals(377762, player.id());
        assertEquals("Aitana BONMATI", player.name());
        assertEquals(6, player.jerseyNumber());
        assertEquals("Midfielder", player.position());
        assertEquals(LocalDate.of(1998, 1, 18), player.dateOfBirth());
    }
}