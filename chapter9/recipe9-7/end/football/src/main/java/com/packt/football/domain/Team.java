package com.packt.football.domain;

import java.util.List;

public class Team {
    private Integer id;
    private String name;
    private List<Player> players;

    public Team(Integer id, String name, List<Player> players) {
        this.id = id;
        this.name = name;
        this.players = players;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
