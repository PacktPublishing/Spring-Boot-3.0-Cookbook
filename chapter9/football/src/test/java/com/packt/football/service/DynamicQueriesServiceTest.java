package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

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
import com.packt.football.domain.Player;
import com.packt.football.domain.TradingUser;
import com.packt.football.domain.User;
import com.packt.football.repo.PlayerEntity;



@SpringBootTest
@Testcontainers
class DynamicQueriesServiceTest {

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
    static void setCassandraProperties(DynamicPropertyRegistry registry) {
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
    DynamicQueriesService dynamicQueriesService;

    @Autowired
    UsersService usersService;

    @Autowired
    AlbumsService albumsService;



    @Test
    public void searchTeamPlayersTest() {
        List<PlayerEntity> players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.of("ila"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        int minHeight = players.stream().map(PlayerEntity::getHeight).min(Integer::compareTo).orElseThrow();
        int maxHeight = players.stream().map(PlayerEntity::getHeight).min(Integer::compareTo).orElseThrow();

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.of("3$@"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.of(minHeight - 1), Optional.of(maxHeight + 1),
                Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.of(190), Optional.of(200),
                Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(40), Optional.of(100));
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(100), Optional.of(140));
        assertThat(players, empty());
    }

    @Test
    public void searchTeamPlayersAndMapTest() {
        List<Player> players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.of("ila"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.of("3$@"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.of(170),
                Optional.of(190), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.of(190),
                Optional.of(200), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(40), Optional.of(100));
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayersAndMap(1884881, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(100), Optional.of(140));
        assertThat(players, empty());
    }

    @Test
    void searchUserMissingPlayers() {
        User user1 = this.usersService.createUser("user1");
        List<PlayerEntity> players = dynamicQueriesService.searchUserMissingPlayers(user1.getId());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(736));

        Album album = albumsService.buyAlbum(user1.getId(), "album1");
        assertNotNull(album);
        List<Card> cards = albumsService.buyCards(user1.getId(), 1);
        assertThat(cards, hasSize(1));
        albumsService.useAllCardAvailable(user1.getId());
        players = dynamicQueriesService.searchUserMissingPlayers(user1.getId());
        assertThat(players, hasSize(735));
    }

    @Test
    void searchUserMissingPlayersAndMap() {
        User user1 = this.usersService.createUser("user1");
        List<Player> players = dynamicQueriesService.searchUserMissingPlayersAndMap(user1.getId());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(736));

        Album album = albumsService.buyAlbum(user1.getId(), "album1");
        assertNotNull(album);
        List<Card> cards = albumsService.buyCards(user1.getId(), 1);
        assertThat(cards, hasSize(1));
        albumsService.useAllCardAvailable(user1.getId());
        players = dynamicQueriesService.searchUserMissingPlayersAndMap(user1.getId());
        assertThat(players, hasSize(735));
    }

    @Test
    void countPlayers() {
        BigInteger count = dynamicQueriesService.countPlayers();
        assertThat(count, not(0));
    }

    @Test
    void findPlayerById(){
        Player player = dynamicQueriesService.findPlayerById(325636);
        assertNotNull(player);
    }

    @Test
    void findUserById() {
        User user1 = usersService.createUser("Test");
        Album album1 = albumsService.buyAlbum(user1.getId(), "sample album");
        List<Card> cards = albumsService.buyCards(user1.getId(), 5);
        Card card = albumsService.addCardToAlbum(cards.get(0).getId(), album1.getId());
        assertThat(card, notNullValue());
        
        TradingUser user = dynamicQueriesService.findUserById(user1.getId());
        assertNotNull(user);
        assertNotNull(user.getAlbums());
        assertThat(user.getAlbums(), hasSize(1));
        assertNotNull(user.getCards());
        assertThat(user.getCards(), hasSize(5));
    }


}