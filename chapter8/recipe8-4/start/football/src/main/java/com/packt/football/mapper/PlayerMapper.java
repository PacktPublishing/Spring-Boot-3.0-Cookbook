package com.packt.football.mapper;

import com.packt.football.domain.Player;
import com.packt.football.repo.PlayerEntity;
public class PlayerMapper {
    public static Player map(PlayerEntity playerEntity) {
        return new Player(playerEntity.getId(), playerEntity.getName(), playerEntity.getJerseyNumber(),
                playerEntity.getPosition(), playerEntity.getDateOfBirth());
    }
}
