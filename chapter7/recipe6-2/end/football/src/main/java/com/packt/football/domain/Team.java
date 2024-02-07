package com.packt.football.domain;

import java.util.List;

public record Team(Integer id, String name, List<Player> players) {
    
}
