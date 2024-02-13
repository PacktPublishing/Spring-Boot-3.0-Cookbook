package com.packt.footballcdb.model;

import java.util.Set;

public record CommentPost(String userId, String targetType, String targetId, String commentContent, Set<String> labels) {
    
}
