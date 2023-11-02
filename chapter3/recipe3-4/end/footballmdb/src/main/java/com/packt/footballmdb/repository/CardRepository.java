package com.packt.footballmdb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String>{
    
}
