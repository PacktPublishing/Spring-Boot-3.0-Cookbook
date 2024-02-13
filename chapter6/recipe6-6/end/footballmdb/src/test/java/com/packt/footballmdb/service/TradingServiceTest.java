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
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
class TradingServiceTest {

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
        mongoDBContainer2.dependsOn(mongoDBContainer1).start();
        mongoDBContainer3.dependsOn(mongoDBContainer2).start();
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
    private TradingService tradingService;
    @Autowired
    private UserService userService;

    @Test
    void exchangeCard() {
        // ARRANGE
        User user1 = new User();
        user1.setUsername("user1");
        user1 = userService.createUser(user1);
        userService.buyTokens(user1.getId(), 10);
        userService.buyCards(user1.getId(), 10);
        List<Card> user1Cards = userService.getUserCards(user1.getId());

        User user2 = new User();
        user2.setUsername("user2");
        user2 = userService.createUser(user2);
        userService.buyTokens(user2.getId(), 10);
        userService.buyCards(user2.getId(), 1);
        List<Card> user2Cards = userService.getUserCards(user2.getId());

        // ACT
        Card card = user1Cards.getFirst();
        tradingService.exchangeCard(card.getId(), user2.getId(), 2);
        // ASSERT
        user1Cards = userService.getUserCards(user1.getId());
        assertTrue(user1Cards.stream().filter(c -> c.getId().equals(card.getId())).findFirst().isEmpty());
        user2Cards = userService.getUserCards(user2.getId());
        assertTrue(user2Cards.stream().filter(c -> c.getId().equals(card.getId())).findFirst().isPresent());

    }

    @Test
    void exchangeCard_detectConflict() throws InterruptedException, ExecutionException {
        // ARRANGE
        User user1 = new User();
        user1.setUsername("user1");
        user1 = userService.createUser(user1);
        userService.buyTokens(user1.getId(), 10000);
        userService.buyCards(user1.getId(), 1);
        List<Card> user1Cards = userService.getUserCards(user1.getId());

        User user2 = new User();
        user2.setUsername("user2");
        user2 = userService.createUser(user2);
        userService.buyTokens(user2.getId(), 10000);
        userService.buyCards(user2.getId(), 1);
        List<Card> user2Cards = userService.getUserCards(user2.getId());
        Card card = user1Cards.getFirst();

        final String user1Id = user1.getId(), user2Id = user2.getId();

        // ACT
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<Boolean> executorCompletionService = new ExecutorCompletionService<>(executorService);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorCompletionService.submit(() -> {
                for(int j=0; j<100; j++) {
                    if (!tradingService.exchangeCard(card.getId(), finalI%2==0 ? user2Id : user1Id, 1 )){
                        return false;
                    }
                }
                return true;
            });
        }

        List<Boolean> results = new ArrayList<>(10);
        for(int i=0; i<10; i++){
            results.add(executorCompletionService.take().get());
        }
        assertTrue(results.stream().anyMatch(r -> r.equals(false)));
    }

    @Test
    void exchangeCard_notEnoughTokens() {
        // ARRANGE
        User user1 = new User();
        user1.setUsername("user1");
        user1 = userService.createUser(user1);
        userService.buyTokens(user1.getId(), 10);
        userService.buyCards(user1.getId(), 10);
        List<Card> user1Cards = userService.getUserCards(user1.getId());

        User user2 = new User();
        user2.setUsername("user2");
        user2 = userService.createUser(user2);
        final String user2Id = user2.getId();


        Card card = user1Cards.getFirst();
        // ACT & ARRANGE
        assertThrows(RuntimeException.class, () -> tradingService.exchangeCard(card.getId(), user2Id, 2));
    }
}