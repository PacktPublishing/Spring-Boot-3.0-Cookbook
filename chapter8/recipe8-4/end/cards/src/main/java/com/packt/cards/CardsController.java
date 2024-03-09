package com.packt.cards;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/cards")
@RestController
public class CardsController {

    @GetMapping
    public Flux<Card> getCards() {
        return Flux.fromIterable(
                List.of(new Card("1", "WWC23", "Ivana Andres", 7), new Card("2", "WWC23", "Alexia Putellas", 1)));
    }

    @GetMapping("/{cardId}")
    public Mono<Card> getCard(@PathVariable String cardId) {
        return Mono.just(new Card(cardId, "WWC23", "Superplayer", 1));
    }

    @GetMapping("/exception")
    public Mono<Card> getException() {
        throw new SampleException("This is a sample exception");
    }

    @ExceptionHandler(SampleException.class)
    public ProblemDetail handleSampleException(SampleException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("sample exception");
        return problemDetail;
    }

}
