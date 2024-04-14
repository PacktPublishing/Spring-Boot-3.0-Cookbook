package com.packt.football.domain;

import java.time.LocalDateTime;

import com.packt.football.repo.MatchEventDetails;

public class MatchEvent {
    private LocalDateTime time;
    private MatchEventDetails details;

    public MatchEvent(LocalDateTime time, MatchEventDetails details) {
        this.time = time;
        this.details = details;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public MatchEventDetails getDetails() {
        return details;
    }

    public void setDetails(MatchEventDetails details) {
        this.details = details;
    }

}
