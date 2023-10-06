(readOnly = true)package com.packt.footballpg;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class FootballService {
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    public FootballService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    
    @Transactional(readOnly = true)
    public Team getTeam(Integer id) {
        TeamEntity team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            return null;
        } else {
            return new Team(team.getId(),
                    team.getName(),
                    team.getPlayers()
                            .stream()
                            .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
                                    player.getDateOfBirth()))
                            .toList());
        }
    }

    public List<Player> searchPlayers(String name) {
        return playerRepository.findByNameContaining(name)
                .stream()
                .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
                        player.getDateOfBirth()))
                .toList();
    }

    public List<Player> searchPlayersByBirthDate(LocalDate date) {
        return playerRepository.findByDateOfBirth(date)
                .stream()
                .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
                        player.getDateOfBirth()))
                .toList();
    }

    public Team createTeam(String name) {
        Random random = new Random();
        TeamEntity team = new TeamEntity();
        Integer randomId = random.nextInt();
        if (randomId < 0) {
            randomId = random.nextInt();
        }
        team.setId(randomId);
        team.setName(name);
        team = teamRepository.save(team);
        return new Team(team.getId(), team.getName(), List.of());
    }

    public Player updatePlayerPosition(Integer id, String position) {
        PlayerEntity player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            return null;
        } else {
            player.setPosition(position);
            player = playerRepository.save(player);
            return new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
                    player.getDateOfBirth());
        }
    }
}
