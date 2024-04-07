package com.packt.football.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.packt.football.domain.Match;
import com.packt.football.domain.MatchEvent;
import com.packt.football.repo.MatchEntity;

@Component
public class MatchMapper {
    public Match map(MatchEntity matchEntity) {
        return new Match(matchEntity.getId(), matchEntity.getTeam1().getName(), matchEntity.getTeam2().getName(),
                matchEntity.getTeam1Goals(), matchEntity.getTeam2Goals(), matchEntity.getMatchDate(),
                matchEntity.getEvents() != null ? matchEntity.getEvents()
                        .stream()
                        .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
                        .collect(Collectors.toList())
                        : List.of());
    }

}
