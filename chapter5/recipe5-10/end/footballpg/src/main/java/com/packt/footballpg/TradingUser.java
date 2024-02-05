package com.packt.footballpg;

import java.util.List;

public record TradingUser(User user, List<Card> cards, List<Album> albums) {
    
}
