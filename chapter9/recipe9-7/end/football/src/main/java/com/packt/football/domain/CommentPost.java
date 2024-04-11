package com.packt.football.domain;

import java.util.Set;

public class CommentPost {
    String userId;
    String targetType;
    String targetId;
    String commentContent;
    Set<String> labels;

    public CommentPost(String userId, String targetType, String targetId, String commentContent, Set<String> labels) {
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.commentContent = commentContent;
        this.labels = labels;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

}
