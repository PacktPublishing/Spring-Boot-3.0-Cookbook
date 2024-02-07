package com.packt.football.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.packt.football.domain.MatchEvent;
import com.packt.football.domain.Player;
import com.packt.football.service.DynamicQueriesService;

@RequestMapping("/queries")
@RestController
public class QueriesController {

    private DynamicQueriesService dynamicQueriesService;

    public QueriesController(DynamicQueriesService dynamicQueriesService) {
        this.dynamicQueriesService = dynamicQueriesService;
    }

    @GetMapping("/team/{teamId}/players")
    public List<Player> searchTeamPlayers(@PathVariable Integer teamId,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Integer> minHeight,
            @RequestParam(required = false) Optional<Integer> maxHeight,
            @RequestParam(required = false) Optional<Integer> minWeight,
            @RequestParam(required = false) Optional<Integer> maxWeight) {
        return dynamicQueriesService.searchTeamPlayersAndMap(teamId, name, minHeight, maxHeight, minWeight, maxWeight);
    }

    @GetMapping("/match/{matchId}/events")
    public List<MatchEvent> getEvents(@PathVariable Integer matchId,
            @RequestParam(required = false) Optional<LocalDateTime> minMinute,
            @RequestParam(required = false) Optional<LocalDateTime> maxMinute) {
        return dynamicQueriesService.searchMatchEventsRangeAndMap(matchId, minMinute, maxMinute);
    }

    @GetMapping("/user/{userId}/missing")
    public List<Player> getMissingPlayers(@PathVariable Integer userId) {
        return dynamicQueriesService.searchUserMissingPlayersAndMap(userId);
    }
}
