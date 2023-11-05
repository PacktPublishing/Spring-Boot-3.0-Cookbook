package com.packt.footballcdb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.packt.footballcdb.model.CommentPost;
import com.packt.footballcdb.repository.Comment;
import com.packt.footballcdb.repository.CommentRepository;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

}
