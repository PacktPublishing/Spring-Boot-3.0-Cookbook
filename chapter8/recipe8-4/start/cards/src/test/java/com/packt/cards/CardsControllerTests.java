package com.packt.cards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CardsController.class)
public class CardsControllerTests {

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

    @Test
    void testGetException() {
        webTestClient
                .get()
                .uri("/cards/exception")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class);
    }

}
