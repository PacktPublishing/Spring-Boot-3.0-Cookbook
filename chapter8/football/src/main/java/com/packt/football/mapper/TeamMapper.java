package com.packt.football.mapper;

import com.packt.football.domain.Team;
import com.packt.football.repo.TeamEntity;

public class TeamMapper {
    public static Team map(TeamEntity teamEntity) {
        return new Team(teamEntity.getId(),
                teamEntity.getName(),
                teamEntity.getPlayers().stream()
                        .map(PlayerMapper::map)
                        .toList());
    }
}
