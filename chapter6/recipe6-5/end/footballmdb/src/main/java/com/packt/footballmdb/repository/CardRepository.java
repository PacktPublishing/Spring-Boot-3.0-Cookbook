package com.packt.footballmdb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String>{
    List<Card> findByOwnerId(String id);
    
}
