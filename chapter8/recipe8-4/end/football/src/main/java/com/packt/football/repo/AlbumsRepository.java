package com.packt.football.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AlbumsRepository extends ReactiveCrudRepository<AlbumEntity, Long> {
}
