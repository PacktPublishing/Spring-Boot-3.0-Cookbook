package com.packt.football.service;

import com.packt.football.domain.Album;
import com.packt.football.domain.Card;
import com.packt.football.domain.TradingUser;
import com.packt.football.domain.User;
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

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = AlbumsServiceTest.Initializer.class)
class AlbumsServiceTest {

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
    public void buyAlbum() {

        // ARRANGE
        User user = usersService.createUser("Test");
        Album album = albumsService.buyAlbum(user.id(), "Sample album");

        assertEquals("Sample album", album.title());
        assertEquals(user.id(), album.ownerId());

    }

    @Test
    public void buyCards() {
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
        Card card = albumsService.addCardToAlbum(cards.getFirst().id(), album1.id());
        assertThat(card.albumId(), notNullValue());
    }

    @Test
    public void useAllCardAvailable() {
        // ARRANGE
        User user = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user.id(), "sample album");
        List<Card> cards = albumsService.buyCards(user.id(), 2);

        // ACT
        List<Card> usedCards = albumsService.useAllCardAvailable(user.id());
        assertThat(usedCards, hasSize(2));
        for (Card card : usedCards) {
            assertThat(card.albumId(), notNullValue());
        }
    }
    @Test
    void transferCard() {
        User user1 = usersService.createUser("Test");
        User user2 = usersService.createUser("Test2");
        Album album1 = albumsService.buyAlbum(user1.id(), "sample album");
        List<Card> cards = albumsService.buyCards(user1.id(), 5);
        Card card = albumsService.addCardToAlbum(cards.getFirst().id(), album1.id());

        Optional<Card> transferredCard = albumsService.transferCard(card.id(), user2.id());
        assertThat(transferredCard, notNullValue());
        assertTrue(transferredCard.flatMap(Card::albumId).isEmpty());
        assertEquals(user2.id(), transferredCard.get().ownerId());
    }

    @Test
    void transferCard_cardNotExists() {
        User user1 = usersService.createUser("Test");
        User user2 = usersService.createUser("Test2");

        Optional<Card> transferredCard = albumsService.transferCard(new Random().nextInt(), user2.id());
        assertTrue(transferredCard.isEmpty());
    }

    @Test
    void transferCard_userNotExists() {
        User user1 = usersService.createUser("Test");
        List<Card> cards = albumsService.buyCards(user1.id(), 5);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, ()-> albumsService.transferCard(cards.getFirst().id(), new Random().nextInt()));
        Optional<TradingUser> userCards = albumsService.getUserWithCardsAndAlbums(user1.id());
        assertTrue(userCards.get().cards().stream().anyMatch(c -> c.id().equals(cards.getFirst().id()) && c.ownerId().equals(user1.id())));
    }

    @Test
    void tradeAllCards() {
        // ARRANGE
        User user1 = usersService.createUser("Test");
        User user2 = usersService.createUser("Test2");
        Album album1 = albumsService.buyAlbum(user1.id(), "sample album");
        Album album2 = albumsService.buyAlbum(user2.id(), "sample album");
        List<Card> cards1 = albumsService.buyCards(user1.id(), 800);
        List<Card> cards2 = albumsService.buyCards(user2.id(), 800);
        albumsService.useAllCardAvailable(user1.id());
        albumsService.useAllCardAvailable(user2.id());

        List<Card> tradedCards = albumsService.tradeAllCards(user1.id(), user2.id());
        assertThat(tradedCards, hasItems());
    }

    @Test
    void getUserWithCardsAndAlbums() {
        User user1 = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user1.id(), "sample album");
        List<Card> cards = albumsService.buyCards(user1.id(), 5);
        Card card = albumsService.addCardToAlbum(cards.getFirst().id(), album1.id());

        Optional<TradingUser> userCards = albumsService.getUserWithCardsAndAlbums(user1.id());
        assertThat(userCards, notNullValue());
        assertThat(userCards.get().cards(), hasSize(5));
        assertThat(userCards.get().albums(), hasSize(1));
    }
}