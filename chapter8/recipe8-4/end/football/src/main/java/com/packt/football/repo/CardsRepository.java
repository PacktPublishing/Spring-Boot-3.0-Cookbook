package com.packt.football.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CardsRepository extends ReactiveCrudRepository<CardEntity, Long> {
}
