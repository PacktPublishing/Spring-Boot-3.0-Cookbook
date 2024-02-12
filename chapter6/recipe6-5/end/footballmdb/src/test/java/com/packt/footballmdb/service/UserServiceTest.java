package com.packt.footballmdb.service;

import com.packt.footballmdb.repository.Card;
import com.packt.footballmdb.repository.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class UserServiceTest {

    private static String[] buildMongoEvalCommand(final String command) {
        return new String[]{
                "mongosh",
                "--eval",
                command
        };
    }

    static Network mongoDbNetwork = Network.newNetwork();

    static GenericContainer<?> mongoDBContainer1 = new GenericContainer<>("mongo:latest")
            .withNetwork(mongoDbNetwork)
            .withCommand("mongod", "--replSet", "rs0", "--port", "27017", "--bind_ip", "localhost,mongo1")
            .withNetworkAliases("mongo1")
            .withExposedPorts(27017)
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/teams.json"), "teams.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/players.json"), "players.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/matches.json"), "matches.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/match_events.json"), "match_events.json");
    ;
    static GenericContainer<?> mongoDBContainer2 = new GenericContainer<>("mongo:latest")
            .withNetwork(mongoDbNetwork)
            .withCommand("mongod", "--replSet", "rs0", "--port", "27017", "--bind_ip", "localhost,mongo2")
            .withNetworkAliases("mongo2")
            .withExposedPorts(27017);

    static GenericContainer<?> mongoDBContainer3 = new GenericContainer<>("mongo:latest")
            .withNetwork(mongoDbNetwork)
            .withCommand("mongod", "--replSet", "rs0", "--port", "27017", "--bind_ip", "localhost,mongo3")
            .withNetworkAliases("mongo3")
            .withExposedPorts(27017);

    @BeforeAll
    static void startContainer() throws IOException, InterruptedException {
        String initCluster = """
                rs.initiate({
                 _id: "rs0",
                 members: [
                   {_id: 0, host: "mongo1"},
                   {_id: 1, host: "mongo2"},
                   {_id: 2, host: "mongo3"}
                 ]
                })
                """;
        mongoDBContainer1.start();
        mongoDBContainer2.start();
        mongoDBContainer3.start();
        for (int i = 0; i < 5; i++) {
            Container.ExecResult res = mongoDBContainer1.execInContainer(buildMongoEvalCommand(initCluster));
            if (res.getExitCode() > 0) {
                Thread.sleep(1000);
            } else {
                break;
            }
        }

        mongoDBContainer1.waitingFor(Wait.forLogMessage("*mongos ready.*", 1));
        importFile(mongoDBContainer1, "matches");
        importFile(mongoDBContainer1, "match_events");
        importFile(mongoDBContainer1, "teams");
        importFile(mongoDBContainer1, "players");
    }

    static void importFile(GenericContainer<?> container, String fileName) throws IOException, InterruptedException {
        String uri = "mongodb://mongo1:27017,mongo2:27017,mongo3:27017/football?replicaSet=rs0";
        Container.ExecResult res = container.execInContainer("mongoimport", "--uri=" + uri, "--db=football", "--collection=" + fileName, "--jsonArray", fileName + ".json");
        if (res.getExitCode() > 0) {
            throw new RuntimeException("MongoDB not properly initialized");
        }
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> {
            String mongoUri = "mongodb://" + mongoDBContainer1.getHost() + ":" + mongoDBContainer1.getMappedPort(27017) + "/?directConnect=true";
            return mongoUri;
        });
    }

    @Autowired
    private UserService userService;

    @Test
    void buyTokens() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        User createdUser = userService.createUser(user);
        // ACT
        Integer numModified = userService.buyTokens(createdUser.getId(), 10);
        // ASSERT
        assertThat(numModified, is(1));
    }

    @Test
    void buyCards() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        User createdUser = userService.createUser(user);
        Integer buyTokens = 10;
        userService.buyTokens(createdUser.getId(), buyTokens);
        Integer requestedCards = 1;
        // ACT
        Integer cardCount = userService.buyCards(user.getId(), requestedCards);
        // ASSERT
        assertThat(cardCount, is(requestedCards));
        List<Card> cards = userService.getUserCards(user.getId());
        assertThat(cards, not(empty()));
        assertThat(cards, hasSize(requestedCards));
        Optional<User> updatedUser = userService.getUser(user.getId());
        assertThat(updatedUser.isPresent(), is(true) );
        assertThat(updatedUser.get().getTokens(), is(buyTokens-requestedCards));

    }

    @Test
    void buyCards_notEnoughTokens() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        User createdUser = userService.createUser(user);
        Integer buyTokens = 10;
        userService.buyTokens(createdUser.getId(), 10);
        Integer requestedCards = buyTokens + 1;
        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> userService.buyCards(user.getId(), requestedCards));
        Optional<User> actualUser = userService.getUser(createdUser.getId());
        assertThat(actualUser, notNullValue());
        assertThat(actualUser.get().getTokens(), is(buyTokens));
        List<Card> cards = userService.getUserCards(createdUser.getId());
        assertThat(cards, is(empty()));


    }

    @Test
    void createUser() {
        // ARRANGE
        User user = new User();
        user.setUsername("Sample user");
        // ACT
        User createdUser = userService.createUser(user);
        // ASSERT
        assertThat(createdUser, notNullValue());
        assertThat(createdUser.getId(), notNullValue());
    }
}