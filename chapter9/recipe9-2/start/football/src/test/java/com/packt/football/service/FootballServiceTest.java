package com.packt.football.service;

import com.packt.football.domain.*;
import com.packt.football.repo.TeamPlayers;
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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = FootballServiceTest.Initializer.class)
class FootballServiceTest {

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
    FootballService footballService;

    @Autowired
    UsersService usersService;
    @Autowired
    AlbumsService albumsService;

    @Test
    void getTeam() {
        Team team = footballService.getTeam(1884823);
        assertThat(team, notNullValue());
        assertThat(team.getPlayers(), not(empty()));
        assertThat(team.getPlayers(), hasSize(23));
    }

    @Test
    void searchPlayers() {
        List<Player> players = footballService.searchPlayers("Alexia");
        assertThat(players, not(empty()));
        assertThat(players, hasSize(1));
    }

    @Test
    void searchPlayersStartingWith() {
        List<Player> players = footballService.searchPlayersStartingWith("Eu");
        assertThat(players, not(empty()));
        assertThat(players, hasSize(2));
    }

    @Test
    void searchPlayersByBirthDate() {
        List<Player> players = footballService.searchPlayersByBirthDate(LocalDate.of(2000, 6, 12));
        assertThat(players, not(empty()));
        assertThat(players, hasSize(2));
    }

    @Test
    void createTeam() {
        Team team1 = footballService.createTeam("Senegal");
        assertThat(team1, notNullValue());
        assertThat(team1.getId(), notNullValue());
    }

    @Test
    void updatePlayerPosition() {
        Player beforePlayer = footballService.getPlayer(396930);
        assertThat(beforePlayer, notNullValue());
        assertThat(beforePlayer.getPosition(), notNullValue());
        assertThat(beforePlayer.getPosition(), not("Midfielder"));
        Player afterPlayer = footballService.updatePlayerPosition(396930, "Midfielder");
        assertThat(afterPlayer, notNullValue());
        assertThat(afterPlayer.getPosition(), notNullValue());
        assertThat(afterPlayer.getPosition(), is("Midfielder"));
    }

    // @Test
    // void getPlayersByMatch() {
    //     List<Player> players = footballService.getPlayersByMatch(400258554);
    //     assertThat(players, not(empty()));
    //     assertThat(players, hasSize(46));
    // }

    @Test
    void getAlbumMissingPlayers() {
        User user1 = this.usersService.createUser("user1");

        Album album = albumsService.buyAlbum(user1.getId(), "album1");
        List<Player> players = footballService.getAlbumMissingPlayers(album.getId());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(736));

        List<Card> cards = albumsService.buyCards(user1.getId(), 1);
        assertThat(cards, hasSize(1));
        albumsService.useAllCardAvailable(user1.getId());
        players = footballService.getAlbumMissingPlayers(album.getId());
        assertThat(players, hasSize(735));
    }

    @Test
    void getAlbumPlayers() {
        User user1 = this.usersService.createUser("user1");
        Album album = albumsService.buyAlbum(user1.getId(), "album1");
        List<Player> players = footballService.getAlbumPlayers(album.getId());
        assertThat(players, empty());

        List<Card> cards = albumsService.buyCards(user1.getId(), 1);
        assertThat(cards, hasSize(1));
        albumsService.useAllCardAvailable(user1.getId());
        players = footballService.getAlbumPlayers(album.getId());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(1));
    }

    @Test
    void getAlbumPlayersByTeam() {
        User user1 = this.usersService.createUser("user1");
        Album album = albumsService.buyAlbum(user1.getId(), "album1");


        List<Card> cards = albumsService.buyCards(user1.getId(), 1);
        cards = albumsService.useAllCardAvailable(user1.getId());
        Team team = footballService.getPlayerTeam(cards.get(0).getPlayer().getId());
        List<Player> players = footballService.getAlbumPlayersByTeam(album.getId(), team.getId());
        assertThat(players, not(empty()));
        assertThat(players, hasSize(1));
    }

    @Test
    void getPlayersList() {
        List<Player> players = footballService.getPlayersList(List.of(415394, 467297));
        assertThat(players, not(empty()));
        assertThat(players, hasSize(2));
        Player player1 = footballService.getPlayer(415394);
        Player player2 = footballService.getPlayer(467297);
        assertTrue(players.stream().anyMatch(p -> p.getId().equals(player1.getId())));
        assertTrue(players.stream().anyMatch(p -> p.getId().equals(player2.getId())));

        players = footballService.getPlayersList(List.of(415394, 467297, 999999));
        assertThat(players, not(empty()));
        assertThat(players, hasSize(2));
        assertTrue(players.stream().anyMatch(p -> p.getId().equals(player1.getId())));
        assertTrue(players.stream().anyMatch(p -> p.getId().equals(player2.getId())));
    }

    @Test
    void searchPlayersLike() {
        List<Player> players = footballService.searchPlayersLike("ann");
        assertThat(players, not(empty()));
        assertThat(players, hasSize(11));
    }

    @Test
    void getTeamPlayers() {
        List<Player> players = footballService.getTeamPlayers(1884823);
        assertThat(players, not(empty()));
        assertThat(players, hasSize(23));
    }

    @Test
    void getAllPlayersPaged() {
        List<Player> players = footballService.getAllPlayersPaged(0, 10);
        assertThat(players, not(empty()));
        assertThat(players, hasSize(10));
        List<Player> secondPage = footballService.getAllPlayersPaged(1, 10);
        assertThat(secondPage, not(empty()));
        assertThat(secondPage, hasSize(10));
        for (Player player : players) {
            assertThat(secondPage, not(hasItem(player)));
        }
    }

    @Test
    void getNumberOfPlayersByPosition() {
        List<TeamPlayers> positionCounts = footballService.getNumberOfPlayersByPosition("Goalkeeper");
        assertThat(positionCounts, not(empty()));
        assertThat(positionCounts, hasSize(32));
    }

    // @Test
    // void getMatchWithTimeline() {
    //     Match match = footballService.getMatchWithTimeline(400258554);
    //     assertThat(match, notNullValue());
    //     assertThat(match.getEvents(), not(empty()));
    //     assertThat(match.getEvents(), hasSize(269));
    // }

    // @Test
    // void getMatchWithPlayerEvents() {
    //     List<MatchEvent> matchEvents = footballService.getMatchWithPlayerEvents(400258554, 413016);
    //     assertThat(matchEvents, not(empty()));
    //     assertThat(matchEvents, hasSize(40));
    // }

    // @Test
    // void getMatchEventsOfType() {
    //     List<MatchEvent> matchEvents = footballService.getMatchEventsOfType(400258554, 0);
    //     assertThat(matchEvents, not(empty()));
    //     assertThat(matchEvents, hasSize(1));
    // }

    // @Test
    // void getTotalPlayersWithMoreThanNMatches() {
    //     Integer total = footballService.getTotalPlayersWithMoreThanNMatches(6);
    //     assertThat(total, notNullValue());
    //     assertThat(total, is(92));
    // }

    // @Test
    // void getMatchWithPlayerEventsError() {
    //     assertThrows(InvalidDataAccessResourceUsageException.class, () -> footballService.getMatchWithPlayerEventsError(400258554, 413016));
    // }

    @Test
    void getTeams() {
        List<Team> teams = footballService.getTeams();
        assertThat(teams, not(empty()));
    }

    @Test
    void getPlayer() {
        Player player = footballService.getPlayer(396914);
        assertThat(player, notNullValue());
        assertThat(player.getName(), is("Laia CODINA"));
    }
}