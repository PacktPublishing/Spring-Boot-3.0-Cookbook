package com.packt.football.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.football.domain.Player;
import com.packt.football.domain.Team;
import com.packt.football.repo.TeamPlayers;
import com.packt.football.service.FootballService;

@RequestMapping("/teams")
@RestController
public class TeamsController {

    private FootballService footballService;

    public TeamsController(FootballService footballService) {
        this.footballService = footballService;
    }

    @GetMapping
    public List<Team> getTeams() {
        return footballService.getTeams();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Integer id) {
        return footballService.getTeam(id);
    }

    @GetMapping("/{id}/players")
    public List<Player> getTeamPlayers(@PathVariable Integer id) {
        return footballService.getTeamPlayers(id);
    }

    @PostMapping
    public Team createTeam(@RequestBody String name) {
        return footballService.createTeam(name);
    }



    @GetMapping("/{position}/count")
    public List<TeamPlayers> getNumberOfPlayersByPosition(@PathVariable String position) {
        return footballService.getNumberOfPlayersByPosition(position);
    }
    
}
