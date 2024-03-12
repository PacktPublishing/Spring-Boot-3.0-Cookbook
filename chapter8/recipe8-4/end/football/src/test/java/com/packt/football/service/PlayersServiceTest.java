package com.packt.football.service;

import com.packt.football.domain.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = PlayersServiceTest.Initializer.class)
class PlayersServiceTest {

    @Autowired
    private PlayersService playersService;

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
    void getPlayers() {
        List<Player> players = playersService.getPlayers().collectList().block();
        assertNotNull(players);
        assertFalse(players.isEmpty());
    }

    @Test
    void getPlayer() {
        Player player = playersService.getPlayer(387138L).block();
        assertNotNull(player);
        assertEquals(387138L, player.id());
        assertEquals("Jennifer HERMOSO", player.name());
        assertEquals(10, player.jerseyNumber());
        assertEquals("Forward", player.position());
        assertEquals("1990-05-09", player.dateOfBirth().toString());
    }

    @Test
    void getPlayerByName() {
        Player player = playersService.getPlayerByName("Jennifer HERMOSO").block();
        assertNotNull(player);
        assertEquals(387138L, player.id());
        assertEquals("Jennifer HERMOSO", player.name());
        assertEquals(10, player.jerseyNumber());
        assertEquals("Forward", player.position());
        assertEquals("1990-05-09", player.dateOfBirth().toString());
    }

    @Test
    void getPlayerByName_notFound() {
        Player player = playersService.getPlayerByName("inexistent name").block();
        assertNull(player);
    }

}