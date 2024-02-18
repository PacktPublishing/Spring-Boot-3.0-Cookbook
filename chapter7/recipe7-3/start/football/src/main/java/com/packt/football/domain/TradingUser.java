package com.packt.football.domain;

import java.util.List;

public record TradingUser(User user, List<Card> cards, List<Album> albums) {
    
}
