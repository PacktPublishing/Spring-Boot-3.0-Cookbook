package com.packt.footballpg;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/football")
@RestController
public class FootballController {

    private FootballService footballService;

    public FootballController(FootballService footballService) {
        this.footballService = footballService;
    }

    @GetMapping("/teams/{id}")
    public Team getTeam(@PathVariable Integer id) {
        return footballService.getTeam(id);
    }

    @GetMapping("/players")
    public List<Player> searchPlayers(@RequestParam String search) {
        return footballService.searchPlayers(search);
    }

    @GetMapping("/players/birth/{date}")
    public List<Player> searchPlayersByBirthDate(@PathVariable LocalDate date) {
        return footballService.searchPlayersByBirthDate(date);
    }

    @PostMapping("/teams")
    public Team createTeam(@RequestBody String name) {
        return footballService.createTeam(name);
    }

    @PutMapping("/player/{id}/position")
    public Player updatePlayerPosition(@PathVariable Integer id, @RequestBody String position) {
        return footballService.updatePlayerPosition(id, position);
    }

}
