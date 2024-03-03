package com.packt.football.mapper;

import org.springframework.stereotype.Component;

import com.packt.football.domain.Player;
import com.packt.football.repo.PlayerEntity;

@Component
public class PlayerMapper {
    public Player map(PlayerEntity playerEntity) {
        return new Player(playerEntity.getId(), playerEntity.getName(), playerEntity.getJerseyNumber(),
                playerEntity.getPosition(), playerEntity.getDateOfBirth());
    }
}
