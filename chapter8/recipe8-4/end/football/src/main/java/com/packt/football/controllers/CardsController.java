package com.packt.football.controllers;

import com.packt.football.domain.Card;
import com.packt.football.service.CardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/cards")
@RestController
public class CardsController {

    private final CardsService cardsService;

    public CardsController(CardsService cardsService) {
        this.cardsService = cardsService;
    }

    @GetMapping
    public Flux<Card> getCards() {
        return cardsService.getCards();
    }

    @GetMapping("/{cardId}")
    public Mono<Card> getCard(@PathVariable Long cardId) {
        return cardsService.getCard(cardId);
    }
}
