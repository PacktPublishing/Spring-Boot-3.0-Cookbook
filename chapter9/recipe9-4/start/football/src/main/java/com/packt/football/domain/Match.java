package com.packt.football.domain;

import java.time.LocalDate;

public class Match {
    private Integer id;
    private String team1;
    private String team2;
    private Integer team1Goals;
    private Integer team2Goals;
    private LocalDate matchDate;
    // private List<MatchEvent> events;

    public Match(Integer id, String team1, String team2, Integer team1Goals2, Integer team2Goals2, LocalDate matchDate /* , List<MatchEvent> events*/) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.team1Goals = team1Goals2;
        this.team2Goals = team2Goals2;
        this.matchDate = matchDate;
        // this.events = events;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public Integer getTeam1Goals() {
        return team1Goals;
    }

    public void setTeam1Goals(Integer team1Goals) {
        this.team1Goals = team1Goals;
    }

    public Integer getTeam2Goals() {
        return team2Goals;
    }

    public void setTeam2Goals(Integer team2Goals) {
        this.team2Goals = team2Goals;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    // public List<MatchEvent> getEvents() {
    //     return events;
    // }

    // public void setEvents(List<MatchEvent> events) {
    //     this.events = events;
    // }

}
