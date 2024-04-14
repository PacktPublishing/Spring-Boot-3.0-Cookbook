package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.packt.football.domain.Album;
import com.packt.football.domain.Card;
import com.packt.football.domain.TradingUser;
import com.packt.football.domain.User;



@Testcontainers
@SpringBootTest

class AlbumsServiceTest {

    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("football")
            .withUsername("football")
            .withPassword("football")
            .withReuse(false);

    @SuppressWarnings({"rawtypes", "resource"})
    static CassandraContainer cassandraContainer = (CassandraContainer) new CassandraContainer("cassandra")
            .withInitScript("createKeyspace.cql")
            .withExposedPorts(9042)
            .withReuse(false);

    @DynamicPropertySource
    static void setDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.cassandra.keyspace-name", () -> "footballKeyspace");
        registry.add("spring.data.cassandra.contact-points", () -> cassandraContainer.getContactPoint().getAddress());
        registry.add("spring.data.cassandra.port", () -> cassandraContainer.getMappedPort(9042));
        registry.add("spring.data.cassandra.local-datacenter", () -> cassandraContainer.getLocalDatacenter());
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
    }

    @BeforeAll
    public static void startContainer() {
        cassandraContainer.start();
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        cassandraContainer.stop();
        postgreSQLContainer.stop();
    }

    

    @Autowired
    AlbumsService albumsService;
    @Autowired
    UsersService usersService;

    @Test
    public void buyAlbum() {

        // ARRANGE
        User user = usersService.createUser("Test");
        Album album = albumsService.buyAlbum(user.getId(), "Sample album");

        assertEquals("Sample album", album.getTitle());
        assertEquals(user.getId(), album.getOwnerId());

    }

    @Test
    public void buyCards() {
        User user = usersService.createUser("Test");
        List<Card> cards = albumsService.buyCards(user.getId(), 5);
        assertThat(cards, hasSize(5));
    }

    @Test
    public void addCardToAlbum() {
        // ARRANGE
        User user = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user.getId(), "sample album");
        List<Card> cards = albumsService.buyCards(user.getId(), 5);

        // ACT
        Card card = albumsService.addCardToAlbum(cards.get(0).getId(), album1.getId());
        assertThat(card.getAlbumId(), notNullValue());
    }

    @Test
    public void useAllCardAvailable() {
        // ARRANGE
        User user = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user.getId(), "sample album");

        List<Card> cards = albumsService.buyCards(user.getId(), 2);

        // ACT & ASSERT
        assertNotNull(album1);
        assertThat(cards, hasSize(2));
        List<Card> usedCards = albumsService.useAllCardAvailable(user.getId());
        assertThat(usedCards, hasSize(2));
        for (Card card : usedCards) {
            assertThat(card.getAlbumId(), notNullValue());
        }

    }

    @Test
    void transferCard() {
        User user1 = usersService.createUser("Test");
        User user2 = usersService.createUser("Test2");
        Album album1 = albumsService.buyAlbum(user1.getId(), "sample album");
        List<Card> cards = albumsService.buyCards(user1.getId(), 5);
        Card card = albumsService.addCardToAlbum(cards.get(0).getId(), album1.getId());

        Optional<Card> transferredCard = albumsService.transferCard(card.getId(), user2.getId());
        assertThat(transferredCard, notNullValue());
        assertNull(transferredCard.get().getAlbumId());
        assertEquals(user2.getId(), transferredCard.get().getOwnerId());
    }

    @Test
    void transferCard_cardNotExists() {
        User user1 = usersService.createUser("Test");
        User user2 = usersService.createUser("Test2");

        assertNotNull(user1);
        assertNotNull(user2);
        Optional<Card> transferredCard = albumsService.transferCard(new Random().nextInt(), user2.getId());
        assertTrue(transferredCard.isEmpty());
    }

    @Test
    void transferCard_userNotExists() {
        User user1 = usersService.createUser("Test");
        List<Card> cards = albumsService.buyCards(user1.getId(), 5);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> albumsService.transferCard(cards.get(0).getId(), new Random().nextInt()));
        Optional<TradingUser> userCards = albumsService.getUserWithCardsAndAlbums(user1.getId());
        assertTrue(userCards.get().getCards().stream().anyMatch(c -> c.getId().equals(cards.get(0).getId()) && c.getOwnerId().equals(user1.getId())));
    }

    @Test
    void tradeAllCards() {
        // ARRANGE
        User user1 = usersService.createUser("Test");
        User user2 = usersService.createUser("Test2");
        Album album1 = albumsService.buyAlbum(user1.getId(), "sample album");
        assertNotNull(album1);
        Album album2 = albumsService.buyAlbum(user2.getId(), "sample album");
        assertNotNull(album2);
        List<Card> cards1 = albumsService.buyCards(user1.getId(), 800);
        List<Card> cards2 = albumsService.buyCards(user2.getId(), 800);
        assertThat(cards1, hasSize(800));
        assertThat(cards2, hasSize(800));
        albumsService.useAllCardAvailable(user1.getId());
        albumsService.useAllCardAvailable(user2.getId());

        List<Card> tradedCards = albumsService.tradeAllCards(user1.getId(), user2.getId());
        assertThat(tradedCards, hasItems());
    }

    @Test
    void getUserWithCardsAndAlbums() {
        User user1 = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user1.getId(), "sample album");
        List<Card> cards = albumsService.buyCards(user1.getId(), 5);
        Card card = albumsService.addCardToAlbum(cards.get(0).getId(), album1.getId());
        assertThat(card, notNullValue());

        Optional<TradingUser> userCards = albumsService.getUserWithCardsAndAlbums(user1.getId());
        assertThat(userCards, notNullValue());
        assertThat(userCards.get().getCards(), hasSize(5));
        assertThat(userCards.get().getAlbums(), hasSize(1));
    }

    @Test
    void getUserSimple() {
        User user1 = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user1.getId(), "sample album");
        List<Card> cards = albumsService.buyCards(user1.getId(), 5);
        Card card = albumsService.addCardToAlbum(cards.get(0).getId(), album1.getId());
        assertThat(card, notNullValue());

        Optional<TradingUser> userCards = albumsService.getUserSimple(user1.getId());
        assertThat(userCards, notNullValue());
        assertThat(userCards.get().getCards(), notNullValue());
        assertThat(userCards.get().getAlbums(), notNullValue());
    }
}