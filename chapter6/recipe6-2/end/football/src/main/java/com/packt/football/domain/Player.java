package com.packt.football.domain;

import java.time.LocalDate;

public record Player(Integer id, String name, Integer jerseyNumber, String position, LocalDate dateOfBirth) {
    
}
