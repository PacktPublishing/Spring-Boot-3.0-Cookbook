package com.packt.football.service;

import com.packt.football.domain.Player;
import com.packt.football.mapper.PlayerMapper;
import com.packt.football.repo.PlayersRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayersService {
    private final PlayersRepository playersRepository;

    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public Flux<Player> getPlayers() {
        return playersRepository.findAll().map(PlayerMapper::map);
    }

    public Mono<Player> getPlayer(Long id) {
        return playersRepository.findById(id)
                .map(PlayerMapper::map);
    }

    public Mono<Player> getPlayerByName(String name) {
        return playersRepository.findByName(name)
                .map(PlayerMapper::map);
    }
}
