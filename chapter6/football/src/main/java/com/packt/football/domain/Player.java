package com.packt.football.domain;

import java.time.LocalDate;

public record Player(String name, Integer jerseyNumber, String position, LocalDate dateOfBirth) {
    
}
