package com.packt.footballpg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.notNull;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = FootballServiceTest.Initializer.class)
public class FootballServiceTest {
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

    @Test
    public void createTeamTest() {
        // ACT
        Team team = footballService.createTeam("Jamaica");
        // ASSERT
        assertThat(team, notNullValue());
    }

    @Test
    public void getTeamsTest() {
        // ARRANGE. As the teams are already create, use the id of any of them
        int teamId = 1884823;

        // ACT&ASSERT: Get the team
        Team team = footballService.getTeam(teamId);
        assertThat(team, notNullValue());
        assertThat(team.players(), notNullValue());
    }
    
    @Test
    public void getTeam_notFound() {
        // ACT&ASSERT: Get a team that does not exist
        assertThat(footballService.getTeam(9999999), nullValue());
    }

    @Test
    public void getPlayers(){
        List<Player> players = footballService.searchPlayers("Adriana");
        assertThat(players, not(empty()));
    }

    @Test
    public void getPlayersByMatch(){
        List<Player> players = footballService.getPlayersByMatch(400222852);
        assertThat(players, not(empty()));  
    }

    @Test
    public void getPlayersByMatch_notFound(){
        List<Player> players = footballService.getPlayersByMatch(9999999);
        assertThat(players, empty());  
    }

    @Test
    public void getPlayersByBirthDate(){
        List<Player> players = footballService.searchPlayersByBirthDate(LocalDate.of(1994, 2, 4));
        assertThat(players, not(empty()));
    }

    @Test
    public void getAlbumMissingPlayers(){
        List<Player> players = footballService.getAlbumMissingPlayers(1);
        assertThat(players, not(empty()));
    }

    @Test
    public void getAlbumPlayers(){
        List<Player> players = footballService.getAlbumPlayers(1);
        assertThat(players, not(empty()));
    }

    @Test
    public void getAlbumPlayersByTeam(){
        List<Player> players = footballService.getAlbumPlayersByTeam(1, 1884881);
        assertThat(players, not(empty()));
    }

    @Test
    public void getPlayersList(){
        List<Player> players = footballService.getPlayersList(List.of(357669, 420326, 420324));
        assertThat(players, not(empty()));
        assertThat(players, hasSize(3));
    }

    @Test
    public void searchPlayersLike(){
        List<Player> players = footballService.searchPlayersLike("Adriana");
        assertThat(players, not(empty()));
    }

    @Test
    public void getTeamPlayers(){
        List<Player> players = footballService.getTeamPlayers(1884823);
        assertThat(players, not(empty()));
    }

    @Test
    public void getTeamPlayers_notFound(){
        List<Player> players = footballService.getTeamPlayers(9999999);
        assertThat(players, empty());
    }

    @Test
    public void getAllPlayersPaged(){
        List<Player> players = footballService.getAllPlayersPaged(0, 10);
        assertThat(players, not(empty()));
        assertThat(players, hasSize(10));
    }

    @Test
    public void getNumberOfPlayersByPosition(){
        List<TeamPlayers> players = footballService.getNumberOfPlayersByPosition("Midfielder");
        assertThat(players, not(empty()));
    }


}
