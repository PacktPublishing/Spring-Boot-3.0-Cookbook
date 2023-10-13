package com.packt.footballpg;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Embeddable;

// @Embeddable
public class MatchEventDetails implements Serializable {
    private Integer type;
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    private String description;
    private List<Integer> players;
    private List<String> mediaFiles;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getPlayers() {
        return players;
    }

    public void setPlayers(List<Integer> players) {
        this.players = players;
    }

    public List<String> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<String> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}
