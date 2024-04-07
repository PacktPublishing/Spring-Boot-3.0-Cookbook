package com.packt.football.domain;

import java.time.LocalDate;

public class Player {
    private Integer id;
    private String name;
    private Integer jerseyNumber;
    private String position;
    private LocalDate dateOfBirth;

    public Player(Integer id2, String name2, Integer jerseyNumber2, String position2, LocalDate dateOfBirth2) {
        this.id = id2;
        this.name = name2;
        this.jerseyNumber = jerseyNumber2;
        this.position = position2;
        this.dateOfBirth = dateOfBirth2;
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

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
