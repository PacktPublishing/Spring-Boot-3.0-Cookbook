package com.packt.matches.controller;

import com.packt.matches.domain.MatchEvent;
import com.packt.matches.service.MatchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/match")
@RestController
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/event")
    public Mono<MatchEvent> createEvent(@RequestBody MatchEvent matchEvent) {
        return matchService.createEvent(matchEvent);
    }
}
