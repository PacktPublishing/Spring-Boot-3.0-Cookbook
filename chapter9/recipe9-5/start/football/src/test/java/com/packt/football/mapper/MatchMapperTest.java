package com.packt.football.mapper;

import com.packt.football.domain.Match;
import com.packt.football.repo.MatchEntity;
import com.packt.football.repo.MatchEventDetails;
import com.packt.football.repo.MatchEventEntity;
import com.packt.football.repo.TeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchMapperTest {

    private MatchMapper matchMapper;

    @BeforeEach
    void setUp() {
        matchMapper = new MatchMapper();
    }

    @Test
    void mapShouldReturnMatchWithCorrectProperties() {
        TeamEntity team1 = new TeamEntity();
        team1.setName("Team1");
        TeamEntity team2 = new TeamEntity();
        team2.setName("Team2");
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(1);
        matchEntity.setTeam1(team1);
        matchEntity.setTeam2(team2);
        matchEntity.setTeam1Goals(3);
        matchEntity.setTeam2Goals(2);
        matchEntity.setMatchDate(LocalDate.now());
        MatchEventEntity eventEntity = new MatchEventEntity();
        eventEntity.setTime(LocalDateTime.now());
        MatchEventDetails details = new MatchEventDetails();
        details.setDescription("Goal");
        details.setPlayers(List.of(1));
        eventEntity.setDetails(details);
        matchEntity.setEvents(List.of(eventEntity));

        Match match = matchMapper.map(matchEntity);

        assertEquals(matchEntity.getId(), match.getId());
        assertEquals(matchEntity.getTeam1().getName(), match.getTeam1());
        assertEquals(matchEntity.getTeam2().getName(), match.getTeam2());
        assertEquals(matchEntity.getTeam1Goals(), match.getTeam1Goals());
        assertEquals(matchEntity.getTeam2Goals(), match.getTeam2Goals());
        assertEquals(matchEntity.getMatchDate(), match.getMatchDate());
        assertEquals(matchEntity.getEvents().size(), match.getEvents().size());
        assertEquals(matchEntity.getEvents().get(0).getTime(), match.getEvents().get(0).getTime());
        assertEquals(matchEntity.getEvents().get(0).getDetails(), match.getEvents().get(0).getDetails());
    }

    @Test
    void mapShouldReturnMatchWithEmptyEventsWhenMatchEntityHasNoEvents() {
        TeamEntity team1 = new TeamEntity();
        team1.setName("Team1");
        TeamEntity team2 = new TeamEntity();
        team2.setName("Team2");

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(1);
        matchEntity.setTeam1(team1);
        matchEntity.setTeam2(team2);
        matchEntity.setTeam1Goals(3);
        matchEntity.setTeam2Goals(2);
        matchEntity.setMatchDate(LocalDate.now());

        Match match = matchMapper.map(matchEntity);

        assertEquals(0, match.getEvents().size());
    }
}