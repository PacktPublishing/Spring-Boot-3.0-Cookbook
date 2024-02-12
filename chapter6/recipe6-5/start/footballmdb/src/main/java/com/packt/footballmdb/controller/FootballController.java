package com.packt.footballmdb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballmdb.repository.MatchEvent;
import com.packt.footballmdb.repository.Player;
import com.packt.footballmdb.repository.Team;
import com.packt.footballmdb.service.FootballService;

@RequestMapping("/football")
@RestController
public class FootballController {
    private FootballService footballService;

    public FootballController(FootballService footballService) {
        this.footballService = footballService;
    }

    @GetMapping("/team/{id}")
    public Team getTeam(@PathVariable String id) {
        return footballService.getTeam(id);
    }

    @GetMapping("/team")
    public Team getTeamByName(@RequestParam String name) {
        return footballService.getTeamByName(name);
    }

    @GetMapping("/team/{name}/contains")
    public List<Team> getTeamsContainingName(@PathVariable String name) {
        return footballService.getTeamsContainingName(name);
    }

    @GetMapping("/player/{id}")
    public Player getPlayer(@PathVariable String id) {
        return footballService.getPlayer(id);
    }

    @PostMapping("/team")
    public Team saveTeam(@RequestBody Team team) {
        return footballService.saveTeam(team);
    }

    @DeleteMapping("/team/{id}")
    public void deleteTeam(@PathVariable String id) {
        footballService.deleteTeam(id);
    }

    @PatchMapping("/team/{id}")
    public void updateTeamName(@PathVariable String id, @RequestParam String name) {
        footballService.updateTeamName(id, name);
    }   

    @GetMapping("/match/{id}/events")
    public List<MatchEvent> getMatchEvents(@PathVariable String id) {
        return footballService.getMatchEvents(id);
    }

    @GetMapping("/match/{matchId}/{playerId}/events")
    public List<MatchEvent> getPlayerEvents(@PathVariable String matchId, @PathVariable String playerId) {
        return footballService.getPlayerEvents(matchId, playerId);
    }

}
