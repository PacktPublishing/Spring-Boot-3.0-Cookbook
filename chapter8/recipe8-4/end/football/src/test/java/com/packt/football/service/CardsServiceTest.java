package com.packt.football.service;

import com.packt.football.domain.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = CardsServiceTest.Initializer.class)
class CardsServiceTest {

    @Autowired
    private CardsService cardsService;

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("football")
            .withUsername("football")
            .withPassword("football");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                            "spring.flyway.url=" + postgreSQLContainer.getJdbcUrl(),
                            "spring.flyway.user=" + postgreSQLContainer.getUsername(),
                            "spring.flyway.password=" + postgreSQLContainer.getPassword(),
                            "spring.r2dbc.url=" + postgreSQLContainer.getJdbcUrl().replace("jdbc:", "r2dbc:"),
                            "spring.r2dbc.username=" + postgreSQLContainer.getUsername(),
                            "spring.r2dbc.password=" + postgreSQLContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    public static void startContainer() {
        postgreSQLContainer.start();
    }

    @Test
    void createCard() {
        // ARRANGE
        Long playerId = 396930L;
        Card card = cardsService.createCard(playerId).block();

        assertNotNull(card);
        assertEquals(playerId, card.player().id());
        assertTrue(card.album().isEmpty());
        assertEquals("Ona BATLLE", card.player().name());
        assertEquals("Defender", card.player().position());
    }

    @Test
    void getCard() {
        // ARRANGE
        Long playerId = 396930L;
        Card card = cardsService.createCard(playerId).block();

        // ACT
        Card retrievedCard = cardsService.getCard(card.id()).block();
        assertNotNull(retrievedCard);
        assertEquals(playerId, retrievedCard.player().id());
        assertTrue(retrievedCard.album().isEmpty());
        assertEquals(card.id(), retrievedCard.id());
        assertEquals("Ona BATLLE", retrievedCard.player().name());
        assertEquals("Defender", retrievedCard.player().position());
    }

    @Test
    void getCards() {
        // ARRANGE
        Long playerId = 396930L;
        Card card = cardsService.createCard(playerId).block();

        // ACT
        List<Card> cards = cardsService.getCards().collectList().block();
        assertTrue(cards.size() > 0);
        Card retrievedCard = cards.stream().filter(c -> c.id().equals(card.id())).findFirst().get();
        assertNotNull(retrievedCard);
        assertEquals(playerId, retrievedCard.player().id());
        assertTrue(retrievedCard.album().isEmpty());

        assertEquals(card.id(), retrievedCard.id());
        assertEquals("Ona BATLLE", retrievedCard.player().name());
        assertEquals("Defender", retrievedCard.player().position());
    }


}