package com.packt.football.domain;

public class Album {
    private Integer id;
    private String title;
    private Integer ownerId;

    public Album() {
    }

    public Album(Integer id, String title, Integer ownerId) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
