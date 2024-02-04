package com.packt.footballpg.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballpg.entities.Team;
import com.packt.footballpg.service.FootballQueryService;



@RestController
@RequestMapping("/football")
public class FootballController {
    private FootballQueryService footballQueryService;

    public FootballController(FootballQueryService footballQueryService) {
        this.footballQueryService = footballQueryService;
    }
    
    @GetMapping("/teamcount")
    public int getTeamCount() {
        return footballQueryService.getTeamCount();
    }

    @GetMapping("/teams")
    public List<Team> getTeams() {
        return footballQueryService.getTeams();
    }

    @GetMapping("/teams/{id}")
    public Team getTeam(@PathVariable int id) {
        return footballQueryService.getTeam(id);
    }
    
}
