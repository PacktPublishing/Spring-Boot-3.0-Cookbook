package com.packt.footballcdb.model;

public record CommentPost(String userId, String targetType, String targetId, String commentContent) {
    
}
