package com.packt.footballmdb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TeamRepository extends MongoRepository<Team, String> {
    public Optional<Team> findByName(String name);

    public List<Team> findByNameContaining(String name);

}
