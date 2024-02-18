package com.packt.football.domain;

import java.time.LocalDateTime;

import com.packt.football.repo.MatchEventDetails;

public record MatchEvent(LocalDateTime time, MatchEventDetails details) {

}
