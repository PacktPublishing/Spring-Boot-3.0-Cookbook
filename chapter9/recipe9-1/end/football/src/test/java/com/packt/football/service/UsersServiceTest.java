package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

import java.util.List;

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

import com.packt.football.domain.User;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = UsersServiceTest.Initializer.class)
class UsersServiceTest {

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
    UsersService usersService;

    @Test
    void createUser() {
        User user1 = usersService.createUser("test");
        assertThat(user1, notNullValue());
        assertThat(user1.getId(), notNullValue());
    }

    @Test
    void getUsers() {
        User user1 = usersService.createUser("test");

        List<User> users = usersService.getUsers();
        assertThat(users, not(empty()));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user1.getId()) && u.getName().equals(user1.getName())));
    }

    @Test
    void getUser() {
        User user1 = usersService.createUser("test");

        User userRetrieved = usersService.getUser(user1.getId());
        assertThat(userRetrieved, notNullValue());
        assertThat(userRetrieved.getId(), is(user1.getId()));
        assertThat(userRetrieved.getName(), is(user1.getName()));
    }
}