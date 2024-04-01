package com.packt.football.service;

import com.packt.football.domain.*;
import com.packt.football.repo.PlayerEntity;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = DynamicQueriesServiceTest.Initializer.class)
class DynamicQueriesServiceTest {

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
        Integer count = dynamicQueriesService.countPlayers();
        assertThat(count, not(0));
    }


}