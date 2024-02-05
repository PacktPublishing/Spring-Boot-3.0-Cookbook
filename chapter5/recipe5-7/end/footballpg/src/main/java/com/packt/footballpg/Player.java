package com.packt.footballpg;

import java.time.LocalDate;

public record Player(String name, Integer jerseyNumber, String position, LocalDate dateOfBirth) {
    
}
