package com.packt.footballpg.service;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.packt.footballpg.entities.Player;

@Service
public class PlayersService {
    private JdbcClient jdbcClient;

    public PlayersService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Player getPlayer(int id) {
        return jdbcClient.sql("SELECT * FROM players WHERE id = :id")
                .param("id", id)
                .query(Player.class)
                .single();
    }

    public List<Player> getPlayers() {
        return jdbcClient.sql("SELECT * FROM players")
                .query(Player.class)
                .list();
    }

    public Player createPlayer(Player player) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
            INSERT INTO players (jersey_number, name, position, date_of_birth, team_id) 
                    VALUES (:jersey_number, :name, :position, :date_of_birth, :team_id)
                    """)
                .param("name", player.getName())
                .param("jersey_number", player.getJerseyNumber())
                .param("position", player.getPosition())
                .param("date_of_birth", player.getDateOfBirth())
                .param("team_id", player.getTeamId())
                .update(keyHolder, "id");
        player.setId(keyHolder.getKey().intValue());
        return player;
    }
}
