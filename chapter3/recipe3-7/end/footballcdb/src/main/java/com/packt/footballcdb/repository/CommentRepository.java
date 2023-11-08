package com.packt.footballcdb.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;


public interface CommentRepository extends CassandraRepository<Comment, String>{

    @AllowFiltering
    List<Comment> findByTargetTypeAndTargetId(String targetType, String targetId);

    @AllowFiltering
    List<Comment> findByDateBetween(LocalDateTime start, LocalDateTime end);

    @AllowFiltering
    List<Comment> findByLabelsContains(String label);

    Optional<Comment> findByCommentId(String commentId);
    
}
