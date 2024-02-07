package com.packt.footballcdb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballcdb.model.CommentPost;
import com.packt.footballcdb.repository.Comment;
import com.packt.footballcdb.service.CommentService;

@RequestMapping("/comment")
@RestController
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment postComment(@RequestBody CommentPost commentPost) {
        return commentService.postComment(commentPost);
    }
        
    @GetMapping
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    @GetMapping("{targetType}/{targetId}")
    public List<Comment> getComments(@PathVariable String targetType, @PathVariable String targetId) {
        return commentService.getComments(targetType, targetId);
    }
    
}
