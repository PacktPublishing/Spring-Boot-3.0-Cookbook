package com.packt.footballpg;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;

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

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = AlbumsServiceTests.Initializer.class)
public class AlbumsServiceTests {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("football")
            .withUsername("football")
            .withPassword("football");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    public static void startContainer() {
        postgreSQLContainer.start();
    }

    @Autowired
    AlbumsService albumsService;
    @Autowired
    UsersService usersService;

    @Test
    public void testBuyAlbum() {

        // ARRANGE
        User user = usersService.createUser("Test");
        Album album = albumsService.buyAlbum(user.id(), "Sample album");

        assertEquals("Sample album", album.title());
        assertEquals(user.id(), album.ownerId());

    }

    @Test
    public void testBuyCards() {
        User user = usersService.createUser("Test");
        List<Card> cards = albumsService.buyCards(user.id(), 5);
        assertThat(cards, hasSize(5));
    }

    @Test
    public void addCardToAlbum() {
        // ARRANGE
        User user = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user.id(), "sample album");
        List<Card> cards = albumsService.buyCards(user.id(), 5);

        // ACT
        Card card = albumsService.addCardToAlbum(cards.get(0).id(), album1.id());
        assertThat(card.albumId(), notNullValue());   
    }

    @Test
    public void useAllCardAvailable() {
        // ARRANGE
        User user = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user.id(), "sample album");
        List<Card> cards = albumsService.buyCards(user.id(), 5);

        // ACT
        List<Card> usedCards = albumsService.useAllCardAvailable(user.id());
        assertThat(usedCards, hasSize(5));
        for (Card card : usedCards) {
            assertThat(card.albumId(), notNullValue());
        }
    }
}