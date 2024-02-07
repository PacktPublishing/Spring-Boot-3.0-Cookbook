package com.packt.footballpg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
@ContextConfiguration(initializers = DynamicQueriesServiceTests.Initializer.class)
public class DynamicQueriesServiceTests {

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

    @Test
    public void searchTeamPlayersTest() {
        List<PlayerEntity> players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.of("ila"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, not(empty()));

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.of("3$@"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(players, empty());

        players = dynamicQueriesService.searchTeamPlayers(1884881, Optional.empty(), Optional.of(170), Optional.of(190),
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
    public void searchMatchEventsRangeTest() {
        List<MatchEventEntity> events = dynamicQueriesService.searchMatchEventsRange(400258556, Optional.empty(),
                Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(258));

        events = dynamicQueriesService.searchMatchEventsRange(400258556,
                Optional.of(LocalDateTime.of(2023, 8, 16, 10, 2, 35)), Optional.empty());
        assertThat(events, not(empty()));
        assertThat(events, hasSize(250));

        events = dynamicQueriesService.searchMatchEventsRange(400258556,
                Optional.empty(), Optional.of(LocalDateTime.of(2023, 8, 16, 10, 2, 35)));
        assertThat(events, not(empty()));
        assertThat(events, hasSize(8));

        events = dynamicQueriesService.searchMatchEventsRange(400258556,
                Optional.of(LocalDateTime.of(2023, 8, 16, 10, 2, 0)),
                Optional.of(LocalDateTime.of(2023, 8, 16, 10, 4, 0)));
        assertThat(events, not(empty()));
        assertThat(events, hasSize(6));

        events = dynamicQueriesService.searchMatchEventsRange(400258556,
                Optional.of(LocalDateTime.of(2024, 8, 16, 10, 2, 0)),
                Optional.of(LocalDateTime.of(2024, 8, 16, 10, 4, 0)));
        assertThat(events, empty());
    }

}
