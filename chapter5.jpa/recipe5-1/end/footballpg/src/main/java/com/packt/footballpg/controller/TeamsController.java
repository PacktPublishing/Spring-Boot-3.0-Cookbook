package com.packt.footballpg.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballpg.entities.Team;
import com.packt.footballpg.service.TeamsService;



@RestController
@RequestMapping("/teams")
public class TeamsController {
    private TeamsService teamsService;

    public TeamsController(TeamsService teamsService) {
        this.teamsService = teamsService;
    }
    
    @GetMapping("/count")
    public int getTeamCount() {
        return teamsService.getTeamCount();
    }

    @GetMapping
    public List<Team> getTeams() {
        return teamsService.getTeams();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable int id) {
        return teamsService.getTeam(id);
    }
    
}
