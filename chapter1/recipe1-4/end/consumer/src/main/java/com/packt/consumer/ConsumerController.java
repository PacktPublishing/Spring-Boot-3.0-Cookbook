package com.packt.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/consumer")
@RestController
public class ConsumerController {
    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/cards")
    public Flux<Card> getCards() {
        return webClient.get().uri("/cards").retrieve().bodyToFlux(Card.class);
    }

    @GetMapping("/cards/{cardId}")
    public Mono<Card> getCard(@PathVariable String cardId) {
        return webClient.get().uri("/cards/" + cardId).retrieve().bodyToMono(Card.class);
    }

    @GetMapping("/error")
    public Mono<String> getFailedRequest() {
        return webClient.get()
                .uri("/invalidpath")
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.NOT_FOUND))
                        return Mono.just("Remote server returned 404");
                    else if (response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                        return Mono.just("Remote server returned 500: " + response.bodyToMono(String.class).block());
                    else
                        return response.bodyToMono(String.class);
                });
    }

}
