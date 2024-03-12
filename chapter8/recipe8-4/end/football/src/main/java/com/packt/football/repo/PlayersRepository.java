package com.packt.football.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface PlayersRepository extends ReactiveCrudRepository<PlayerEntity, Long> {
    public Mono<PlayerEntity> findByName(String name);
}
