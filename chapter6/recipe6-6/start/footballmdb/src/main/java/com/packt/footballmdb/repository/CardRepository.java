package com.packt.footballmdb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String>{
    List<Card> findByOwnerId(String id);
    
}
