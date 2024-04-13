package com.packt.football.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.packt.football.configuration.SecurityConfig;
import com.packt.football.domain.Player;
import com.packt.football.service.FootballService;

@WebMvcTest(FootballController.class)
@Import(SecurityConfig.class)
class FootballControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    FootballService footballService;

    @Test
    void getPlayersByMatch() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990, 1, 1));
        List<Player> players = List.of(player);
        given(footballService.getPlayersByMatch(1)).willReturn(players);

        mvc.perform(get("/football/matches/1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPlayersByMatch_withTrailSlash() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990, 1, 1));
        List<Player> players = List.of(player);
        given(footballService.getPlayersByMatch(1)).willReturn(players);

        mvc.perform(get("/football/matches/1/players/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAlbumTeamPlayers() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayersByTeam(1, 1)).willReturn(players);

        mvc.perform(get("/football/albums/1/1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAlbumTeamPlayers_withTrailSlash() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayersByTeam(1, 1)).willReturn(players);

        mvc.perform(get("/football/albums/1/1/players/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAlbumMissingPlayers() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumMissingPlayers(1)).willReturn(players);

        mvc.perform(get("/football/albums/1/missingplayers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAlbumMissingPlayers_withTrailSlash() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumMissingPlayers(1)).willReturn(players);

        mvc.perform(get("/football/albums/1/missingplayers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAlbumMyPlayers() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayers(1)).willReturn(players);

        mvc.perform(get("/football/albums/1/myplayers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        
    }

    @Test
    void getAlbumMyPlayers_withTrailSlash() throws Exception {
        Player player = new Player(1, "Player 1", 1, "Goalkeeper", LocalDate.of(1990,
                1, 1));
        List<Player> players = List.of(player);
        given(footballService.getAlbumPlayers(1)).willReturn(players);

        mvc.perform(get("/football/albums/1/myplayers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getMatchWithTimeline() throws Exception {
        given(footballService.getMatchWithTimeline(1)).willReturn(Optional.empty());

        mvc.perform(get("/football/matches/1/timeline"))
                .andExpect(status().isOk());
    }

    @Test
    void getMatchWithTimeline_withTrailSlash() throws Exception {
        given(footballService.getMatchWithTimeline(1)).willReturn(Optional.empty());

        mvc.perform(get("/football/matches/1/timeline/"))
                .andExpect(status().isOk());
    }

    @Test
    void getMatchWithEventsOfType() throws Exception {
        given(footballService.getMatchEventsOfType(1, 1)).willReturn(List.of());

        mvc.perform(get("/football/matches/1/timeline/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        
    }

    @Test
    void getMatchWithEventsOfType_withTrailSlash() throws Exception {
        given(footballService.getMatchEventsOfType(1, 1)).willReturn(List.of());

        mvc.perform(get("/football/matches/1/timeline/events/1/"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getTotalPlayersWithMoreThanNMatches() throws Exception {
        given(footballService.getTotalPlayersWithMoreThanNMatches(5)).willReturn(10);

        mvc.perform(get("/football/players/matches/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void getTotalPlayersWithMoreThanNMatches_withTrailSlash() throws Exception {
        given(footballService.getTotalPlayersWithMoreThanNMatches(5)).willReturn(10);

        mvc.perform(get("/football/players/matches/5/"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

}