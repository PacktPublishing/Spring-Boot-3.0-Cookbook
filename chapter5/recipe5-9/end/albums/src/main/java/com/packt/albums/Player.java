package com.packt.albums;

import java.time.LocalDate;

public record Player(String id, Integer jerseyNumber, String name, String position, LocalDate dateOfBirth) {
}
