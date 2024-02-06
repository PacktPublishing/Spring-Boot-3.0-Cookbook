package com.packt.footballpg;

import java.util.Optional;

public record Card(Integer id, Integer ownerId, Optional<Integer> albumId, Player player) {

}
