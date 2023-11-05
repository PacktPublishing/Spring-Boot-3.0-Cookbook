package com.packt.footballcdb.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface CommentRepository extends CassandraRepository<Comment, String>{

    @AllowFiltering
    List<Comment> findByTargetTypeAndTargetId(String targetType, String targetId);
    
}
