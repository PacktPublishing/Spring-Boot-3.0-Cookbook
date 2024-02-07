package com.packt.footballpg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;

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
@ContextConfiguration(initializers = UsersServiceTest.Initializer.class)
public class UsersServiceTest {
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
    public void testCreateUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("test");
        userEntity.setId(1);

        User user = usersService.createUser("test");

        assertEquals("test", user.name());
    }

    @Test
    public void testGetUsers() {
        usersService.createUser("test1");
        usersService.createUser("test2");
        List<User> users = usersService.getUsers();
        assertEquals(2, users.size());
        assertEquals("test1", users.get(0).name());
        assertEquals("test2", users.get(1).name());
    }

    @Test
    public void testGetUser() {
        User user1 = usersService.createUser("test1");

        User user = usersService.getUser(user1.id());

        assertEquals("test1", user.name());
        assertEquals(user1.id(), user.id());
        assertEquals(user1, user);
    }

    @Test
    public void testGetUserNotFound() {

        assertThrows(NoSuchElementException.class, () -> usersService.getUser(888888));
    }
}
