package com.packt.football.mapper;

import com.packt.football.domain.Team;
import com.packt.football.repo.PlayerEntity;
import com.packt.football.repo.TeamEntity;

import java.util.stream.Stream;

public class TeamMapper {
    public static Team map(TeamEntity teamEntity, Stream<PlayerEntity> players) {
        return new Team(teamEntity.getId(),
                teamEntity.getName(),
                players.map(PlayerMapper::map)
                        .toList());
    }
}
