package com.packt.football.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.packt.football.domain.Player;
import com.packt.football.service.FootballService;

@RequestMapping("/players")
@RestController
public class PlayersController {
    private FootballService footballService;

    public PlayersController(FootballService footballService) {
        this.footballService = footballService;
    }

    @GetMapping
    public List<Player> searchPlayers(@RequestParam String search) {
        return footballService.searchPlayers(search);
    }

    @Cacheable(value = "players")
    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Integer id) {
        return footballService.getPlayer(id);
    }

    @GetMapping("/birth/{date}")
    public List<Player> searchPlayersByBirthDate(@PathVariable LocalDate date) {
        return footballService.searchPlayersByBirthDate(date);
    }

    @PutMapping("/{id}/position")
    public Player updatePlayerPosition(@PathVariable Integer id, @RequestBody String position) {
        return footballService.updatePlayerPosition(id, position);
    }

    @GetMapping("/list")
    public List<Player> getPlayersList(@RequestParam List<Integer> players) {
        return footballService.getPlayersList(players);
    }

    @GetMapping("/startwith")
    public List<Player> searchPlayersStartingWith(@RequestParam String startingName) {
        return footballService.searchPlayersStartingWith(startingName);
    }

    @GetMapping("/search")
    public List<Player> searchPlayersLike(@RequestParam String q) {
        return footballService.searchPlayersLike(q);
    }

    @GetMapping("/paginated")
    public List<Player> getPlayers(@RequestParam Map<String, String> params) {
        Integer page = Integer.parseInt(params.getOrDefault("page", "0"));
        Integer size = Integer.parseInt(params.getOrDefault("size", "10"));
        return footballService.getAllPlayersPaged(page, size);
    }
}
