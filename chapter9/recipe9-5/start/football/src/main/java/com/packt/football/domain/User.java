package com.packt.football.domain;

public class User {
    private Integer id;
    private String name;

    public User(Integer id, String username) {
        this.id = id;
        this.name = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
