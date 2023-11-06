package com.packt.footballcdb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import java.util.Set;


public interface CommentRepository extends CassandraRepository<Comment, String>{

    @AllowFiltering
    List<Comment> findByTargetTypeAndTargetId(String targetType, String targetId);

    @AllowFiltering
    List<Comment> findByDateBetween(LocalDateTime start, LocalDateTime end);

    @AllowFiltering
    List<Comment> findByLabelsContains(String label);
    
}
