package com.packt.football.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.packt.football.domain.MatchEvent;
import com.packt.football.domain.Player;
import com.packt.football.service.FootballService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FootballControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    FootballService footballService;

    @Test
    void getPlayersByMatch() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990, 1, 1));
        List<Player> players = List.of(player);
        given(footballService.getPlayersByMatch(1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/matches/1/players", Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getPlayersByMatch_withTrailSlash() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990, 1, 1));
        List<Player> players = List.of(player);
        given(footballService.getPlayersByMatch(1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/matches/1/players/", Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getAlbumTeamPlayers() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayersByTeam(1, 1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/albums/1/1/players", Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getAlbumTeamPlayers_withTrailSlash() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayersByTeam(1, 1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/albums/1/1/players/", Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getAlbumMissingPlayers() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumMissingPlayers(1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/albums/1/missingplayers",
                Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getAlbumMissingPlayers_withTrailSlash() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumMissingPlayers(1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/albums/1/missingplayers/",
                Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getAlbumMyPlayers() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayers(1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/albums/1/myplayers",
                Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getAlbumMyPlayers_withTrailSlash() {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayers(1)).willReturn(players);

        ResponseEntity<Player[]> result = restTemplate.getForEntity("/football/albums/1/myplayers/",
                Player[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    void getMatchWithTimeline() {
        given(footballService.getMatchWithTimeline(1)).willReturn(Optional.empty());

        ResponseEntity<Match> result = restTemplate.getForEntity("/football/matches/1/timeline",
                Match.class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertNull(result.getBody());
    }

    @Test
    void getMatchWithTimeline_withTrailSlash() {
        given(footballService.getMatchWithTimeline(1)).willReturn(Optional.empty());

        ResponseEntity<Match> result = restTemplate.getForEntity("/football/matches/1/timeline/",
                Match.class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertNull(result.getBody());
    }

    @Test
    void getMatchWithEventsOfType() {
        given(footballService.getMatchEventsOfType(1, 1)).willReturn(List.of());

        ResponseEntity<MatchEvent[]> result = restTemplate.getForEntity("/football/matches/1/timeline/events/1",
                MatchEvent[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(0, result.getBody().length);
    }

    @Test
    void getMatchWithEventsOfType_withTrailSlash() {
        given(footballService.getMatchEventsOfType(1, 1)).willReturn(List.of());

        ResponseEntity<MatchEvent[]> result = restTemplate.getForEntity("/football/matches/1/timeline/events/1/",
                MatchEvent[].class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(0, result.getBody().length);
    }

    @Test
    void getTotalPlayersWithMoreThanNMatches() {
        given(footballService.getTotalPlayersWithMoreThanNMatches(5)).willReturn(10);

        ResponseEntity<Integer> result = restTemplate.getForEntity("/football/players/matches/5",
                Integer.class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody() == 10);

    }

    @Test
    void getTotalPlayersWithMoreThanNMatches_withTrailSlash() {
        given(footballService.getTotalPlayersWithMoreThanNMatches(5)).willReturn(10);

        ResponseEntity<Integer> result = restTemplate.getForEntity("/football/players/matches/5/",
                Integer.class);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(result.getBody() == 10);
    }

}