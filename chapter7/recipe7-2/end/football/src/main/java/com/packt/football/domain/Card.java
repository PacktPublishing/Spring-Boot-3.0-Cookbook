package com.packt.football.domain;

import java.util.Optional;

public record Card(Integer id, Integer ownerId, Optional<Integer> albumId, Player player) {

}
