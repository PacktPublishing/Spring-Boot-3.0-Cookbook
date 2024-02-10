package com.packt.footballmdb.service;

import com.packt.footballmdb.repository.Player;
import com.packt.footballmdb.repository.Team;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SpringBootTest
@Testcontainers
class FootballServiceTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/teams.json"), "teams.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/players.json"), "players.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/matches.json"), "matches.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/events.json"), "events.json");

    @BeforeAll
    static void startContainer() throws IOException, InterruptedException {
        mongoDBContainer.start();
        importFile("teams");
        importFile("players");
        importFile("matches");
        importFile("events");
    }

    static void importFile(String fileName) throws IOException, InterruptedException {
        Container.ExecResult res = mongoDBContainer.execInContainer("mongoimport", "--db=football", "--collection=" + fileName, "--jsonArray", fileName + ".json");
        if (res.getExitCode() > 0){
            throw new RuntimeException("MongoDB not properly initialized");
        }
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private FootballService footballService;

    @Test
    void getTeam() {
        Team team = footballService.getTeam("1884881");
        assertNotNull(team);
    }

    @Test
    void getTeam_notExists() {
        Team team = footballService.getTeam("99999999");
        assertNull(team);
    }

    @Test
    void getTeamByName() {
        Team team = footballService.getTeamByName("Argentina");
        assertNotNull(team);
    }

    @Test
    void getTeamsContainingName() {
        List<Team> teams = footballService.getTeamsContainingName("land");
        assertNotNull(teams);
        assertThat(teams, not(empty()));
        assertThat(teams, hasSize(5));
    }

    @Test
    void getPlayer() {
        Player player = footballService.getPlayer("387138");
        assertThat(player, notNullValue());
    }

    @Test
    void saveTeam() {
        // ACT
        Team t = new Team();
        t.setName("Senegal");
        Team savedTeam = footballService.saveTeam(t);
        // ASSERT
        assertThat(savedTeam, notNullValue());
        assertThat(savedTeam.getId(), notNullValue());
        Team retreivedTeam = footballService.getTeam(savedTeam.getId());
        assertThat(retreivedTeam, notNullValue());
        // CLEAN-UP
        footballService.deleteTeam(retreivedTeam.getId());
    }

    @Test
    void deleteTeam() {
        // ARRANGE
        Team t = new Team();
        t.setName("Senegal");
        Team savedTeam = footballService.saveTeam(t);
        // ACT
        footballService.deleteTeam(savedTeam.getId());
        // ASSERT
        Team deletedTeam = footballService.getTeam(savedTeam.getId());
        assertThat(deletedTeam, nullValue());
    }

    @Test
    void updateTeamName() {
    }


}