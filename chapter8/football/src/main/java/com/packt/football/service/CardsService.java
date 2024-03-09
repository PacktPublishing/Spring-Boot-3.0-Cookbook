package com.packt.football.service;

import com.packt.football.domain.Card;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardsService {
    public Mono<Card> getCard(String cardId) {
    }

    public Flux<Card> getCards() {

    }
}
