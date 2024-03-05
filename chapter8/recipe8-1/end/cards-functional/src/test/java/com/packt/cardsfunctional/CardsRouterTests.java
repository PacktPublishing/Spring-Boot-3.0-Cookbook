package com.packt.cardsfunctional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CardsRouterConfig.class)
public class CardsRouterTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testGetCards() {
        webTestClient.get()
                .uri("/cards")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Card.class);
    }

    @Test
    void testGetCard() {
        webTestClient
                .get()
                .uri("/cards/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Card.class);
    }
}
