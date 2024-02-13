package com.packt.footballcdb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.UpdateOptions;
import org.springframework.data.cassandra.core.query.ColumnName;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.data.cassandra.core.query.CriteriaDefinitions;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.packt.footballcdb.model.CommentPost;
import com.packt.footballcdb.repository.Comment;
import com.packt.footballcdb.repository.CommentRepository;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private CassandraTemplate cassandraTemplate;

    public CommentService(CommentRepository commentRepository, CassandraTemplate cassandraTemplate) {
        this.commentRepository = commentRepository;
        this.cassandraTemplate = cassandraTemplate;
    }

    public List<Comment> getComments(String targetType, String targetId) {
        return commentRepository.findByTargetTypeAndTargetId(targetId, targetType);
    }

    public Comment postComment(CommentPost commentPost) {
        Comment comment = new Comment();
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setUserId(commentPost.userId());
        comment.setTargetType(commentPost.targetType());
        comment.setTargetId(commentPost.targetId());
        comment.setContent(commentPost.commentContent());
        comment.setDate(LocalDateTime.now());
        comment.setLabels(commentPost.labels());
        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getComments(String targetType, String targetId, Optional<String> userId,
            Optional<LocalDateTime> start, Optional<LocalDateTime> end, Optional<Set<String>> labels) {
        Select select = QueryBuilder.selectFrom("comment").all()
                .whereColumn("targetType").isEqualTo(QueryBuilder.literal(targetType))
                .whereColumn("targetId").isEqualTo(QueryBuilder.literal(targetId));
        if (userId.isPresent()) {
            select = select.whereColumn("userId").isEqualTo(QueryBuilder.literal(userId.get()));
        }
        if (start.isPresent()) {
            select = select.whereColumn("date").isGreaterThan(QueryBuilder.literal(start.get().toString()));
        }
        if (end.isPresent()) {
            select = select.whereColumn("date").isLessThan(QueryBuilder.literal(end.get().toString()));
        }
        if (labels.isPresent()) {
            for (String label : labels.get()) {
                select = select.whereColumn("labels").contains(QueryBuilder.literal(label));
            }
        }
        return cassandraTemplate.select(select.allowFiltering().build(), Comment.class);
    }

    public List<Comment> getCommentsString(String targetType, String targetId, Optional<String> userId,
            Optional<LocalDateTime> start, Optional<LocalDateTime> end, Optional<Set<String>> labels) {
        String query = "SELECT * FROM comment WHERE targetType ='" + targetType + "' AND targetId='" + targetId + "'";
        if (userId.isPresent()) {
            query += " AND userId='" + userId.get() + "'";
        }
        if (start.isPresent()) {
            query += " AND date > '" + start.get().toString() + "'";
        }
        if (end.isPresent()) {
            query += " AND date < '" + end.get().toString() + "'";
        }
        if (labels.isPresent()) {
            for (String label : labels.get()) {
                query += " AND labels CONTAINS '" + label + "'";
            }
        }

        query += " ALLOW FILTERING";
        return cassandraTemplate.select(query, Comment.class);
    }

    public Comment upvoteComment(String commentId) {
        Random rnd = new Random();
        // let's retry up to 3 times
        for (int i = 0; i < 3; i++) {
            Comment comment = commentRepository.findByCommentId(commentId).get();
            Integer currentVotes = comment.getUpvotes();
            if (currentVotes == null) {
                comment.setUpvotes(1);    
            } else {
                comment.setUpvotes(currentVotes + 1);
            }            
            CriteriaDefinition ifCriteria = Criteria.where(ColumnName.from("upvotes")).is(currentVotes);
            EntityWriteResult<Comment> result = cassandraTemplate.update(comment,
                    UpdateOptions.builder().ifCondition(ifCriteria).build());
            if (result.wasApplied()) {
                return result.getEntity();
            } else {
                try {
                    Thread.sleep(rnd.nextInt(5, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } // wait between 5 and 100 ms before retrying
            }
        }
        throw new IllegalStateException("comment " + commentId
                + " was updated concurrently and could not be upvoted. Wait few moments and try again.");

    }

}
