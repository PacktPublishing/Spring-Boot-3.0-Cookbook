package com.packt.football.repo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate matchDate;

    @ManyToOne
    @JoinColumn(name = "team1_id", nullable = false)
    private TeamEntity team1;

    @ManyToOne
    @JoinColumn(name = "team2_id", nullable = false)
    private TeamEntity team2;

    @Column(name = "team1_goals", columnDefinition = "integer default 0")
    private Integer team1Goals;
    @Column(name = "team2_goals", columnDefinition = "integer default 0")
    private Integer team2Goals;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY)
    private List<MatchEventEntity> events;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public TeamEntity getTeam1() {
        return team1;
    }

    public void setTeam1(TeamEntity team1) {
        this.team1 = team1;
    }

    public TeamEntity getTeam2() {
        return team2;
    }

    public void setTeam2(TeamEntity team2) {
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

    public List<MatchEventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<MatchEventEntity> events) {
        this.events = events;
    }

}
