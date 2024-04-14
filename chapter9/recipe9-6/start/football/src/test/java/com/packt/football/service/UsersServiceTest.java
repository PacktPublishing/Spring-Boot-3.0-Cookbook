package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

import java.util.List;

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

import com.packt.football.domain.User;



@SpringBootTest
@Testcontainers
class UsersServiceTest {

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
        registry.add("spring.cassandra.keyspace-name", () -> "footballKeyspace");
        registry.add("spring.cassandra.contact-points", () -> cassandraContainer.getContactPoint().getAddress());
        registry.add("spring.cassandra.port", () -> cassandraContainer.getMappedPort(9042));
        registry.add("spring.cassandra.local-datacenter", () -> cassandraContainer.getLocalDatacenter());
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