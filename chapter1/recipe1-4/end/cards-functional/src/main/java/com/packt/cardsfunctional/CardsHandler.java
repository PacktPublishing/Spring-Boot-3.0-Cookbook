package com.packt.cardsfunctional;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CardsHandler {
    public Flux<Card> getCards() {
        return Flux.fromIterable(
                List.of(new Card("1", "WWC23", "Ivana Andres", 7), new Card("2", "WWC23", "Alexia Putellas", 1)));
    }

    public Mono<Card> getCard(String cardId) {
        return Mono.just(new Card(cardId, "WWC23", "Superplayer", 1));
    }
}