package com.packt.football.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.football.domain.Match;
import com.packt.football.domain.MatchEvent;
import com.packt.football.domain.Player;
import com.packt.football.service.FootballService;

@RequestMapping("/football")
@RestController
public class FootballController {

    private FootballService footballService;

    public FootballController(FootballService footballService) {
        this.footballService = footballService;
    }

    @GetMapping("/matches/{id}/players")
    public List<Player> getPlayersByMatch(@PathVariable Integer id) {
        return footballService.getPlayersByMatch(id);
    }

    @GetMapping("/albums/{id}/{teamId}/players")
    public List<Player> getAlbumTeamPlayers(@PathVariable Integer id, @PathVariable Integer teamId) {
        return footballService.getAlbumPlayersByTeam(id, teamId);
    }

    @GetMapping("/albums/{id}/missingplayers")
    public List<Player> getAlbumMissingPlayers(@PathVariable Integer id) {
        return footballService.getAlbumMissingPlayers(id);
    }

    @GetMapping("/albums/{id}/myplayers")
    public List<Player> getAlbumMyPlayers(@PathVariable Integer id) {
        return footballService.getAlbumPlayers(id);
    }

    

    @GetMapping("/matches/{id}/timeline")
    public Match getMatchWithTimeline(@PathVariable Integer id) {
        return footballService.getMatchWithTimeline(id);
    }

    @GetMapping("/matches/{id}/timeline/{playerId}")
    public List<MatchEvent> getMatchWithTimeline(@PathVariable Integer id, @PathVariable Integer playerId) {
        return footballService.getMatchWithPlayerEvents(id, playerId);
    }
    @GetMapping("/matches/{id}/timeline/events/{type}")
    public List<MatchEvent> getMatchWithEventsOfType(@PathVariable Integer id, @PathVariable Integer type) {
        return footballService.getMatchEventsOfType(id, type);
    }

    @GetMapping("/players/matches/{numMatches}")
    public Integer getTotalPlayersWithMoreThanNMatches(@PathVariable Integer numMatches) {
        return footballService.getTotalPlayersWithMoreThanNMatches(numMatches);
    }

    @GetMapping("/matches/{id}/timeline/{playerId}/error")
    public List<MatchEvent> getMatchWithTimelineError(@PathVariable Integer id, @PathVariable Integer playerId) {
        return footballService.getMatchWithPlayerEventsError(id, playerId);
    }
}
