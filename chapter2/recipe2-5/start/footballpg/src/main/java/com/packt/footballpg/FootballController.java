package com.packt.footballpg;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/teams/{id}/players")
    public List<Player> getTeamPlayers(@PathVariable Integer id) {
        return footballService.getTeamPlayers(id);
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

    @PutMapping("/players/{id}/position")
    public Player updatePlayerPosition(@PathVariable Integer id, @RequestBody String position) {
        return footballService.updatePlayerPosition(id, position);
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

    @GetMapping("/players/list")
    public List<Player> getPlayersList(@RequestParam List<Integer> players) {
        return footballService.getPlayersList(players);
    }

    @GetMapping("/players/startwith")
    public List<Player> searchPlayersStartingWith(@RequestParam String startingName) {
        return footballService.searchPlayersStartingWith(startingName);
    }

    @GetMapping("/players/search")
    public List<Player> searchPlayersLike(@RequestParam String q) {
        return footballService.searchPlayersLike(q);
    }

    @GetMapping("/players/paginated")
    public List<Player> getPlayers(@RequestParam Map<String, String> params) {
        Integer page = Integer.parseInt(params.getOrDefault("page", "0"));
        Integer size = Integer.parseInt(params.getOrDefault("size", "10"));
        return footballService.getAllPlayersPaged(page, size);
    }

    @GetMapping("/teams/{position}/count")
    public List<TeamPlayers> getNumberOfPlayersByPosition(@PathVariable String position) {
        return footballService.getNumberOfPlayersByPosition(position);
    }

}
