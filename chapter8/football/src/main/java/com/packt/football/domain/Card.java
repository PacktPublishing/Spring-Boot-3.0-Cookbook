package com.packt.football.domain;

import java.util.Optional;

public record Card(Integer id, Optional<Album> album, Player player) {

}
