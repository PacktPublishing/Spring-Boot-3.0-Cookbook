package com.packt.football.domain;

import java.util.Optional;

public record Card(Long id, Optional<Album> album, Player player) {

}
