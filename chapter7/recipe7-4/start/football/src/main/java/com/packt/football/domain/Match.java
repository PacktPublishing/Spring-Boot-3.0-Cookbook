package com.packt.football.domain;

import java.time.LocalDate;
import java.util.List;

public record Match(Integer id, String team1, String team2, Integer team1Goals, Integer team2Goals, LocalDate matchDate, List<MatchEvent> events) {
    
}
