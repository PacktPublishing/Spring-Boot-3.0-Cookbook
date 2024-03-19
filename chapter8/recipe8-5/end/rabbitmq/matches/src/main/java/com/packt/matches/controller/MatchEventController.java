package com.packt.matches.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.matches.domain.MatchEvent;
import com.packt.matches.service.MatchService;

@RequestMapping("/match")
@RestController
public class MatchEventController {

    private final MatchService matchService;

    public MatchEventController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody MatchEvent matchEvent) {
        try {
            matchService.createEvent(matchEvent);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
