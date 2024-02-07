package com.packt.footballpg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.notNull;

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
        Team team2 = footballService.getTeam(team.id());
        assertThat(team2, notNullValue());
        assertThat(team2, is(team));
    }

    @Test
    public void getTeamsTest() {
        // ARRANGE: Create a team
        Team team = footballService.createTeam("Jamaica");
        // ACT&ASSERT: Get the team
        assertThat(footballService.getTeam(team.id()), notNullValue());
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

}
