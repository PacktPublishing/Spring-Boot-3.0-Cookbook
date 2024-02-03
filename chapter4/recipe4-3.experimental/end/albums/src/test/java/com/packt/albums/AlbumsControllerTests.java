package com.packt.albums;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest
// @ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
public class AlbumsControllerTests {

    @Autowired
    MockMvc mvc;

    static Network network = Network.newNetwork().SHARED;

    @Container
    static GenericContainer<?> eurekaContainer = new GenericContainer<>("registry:0.0.1-SNAPSHOT")
            .withNetworkMode("host");

    @Container
    static GenericContainer<?> footballContainer = new GenericContainer<>("football:0.0.1-SNAPSHOT")
            .dependsOn(eurekaContainer)
            .withNetworkMode("host")
            .waitingFor(Wait.forLogMessage(
                    ".*- registration status: 204.*", 1));

    // @Container
    // static GenericContainer<?> eurekaContainer = new GenericContainer<>("registry:0.0.1-SNAPSHOT")
    //         .withNetwork(network)
    //         .withAccessToHost(true)
    //         .withNetworkMode("host")
    //         .withNetworkAliases("registry")
    //         .withExposedPorts(8761);

    // @Container
    // static GenericContainer<?> footballContainer = new GenericContainer<>("football:0.0.1-SNAPSHOT")
    //         .withEnv("eureka.client.serviceUrl.defaultZone", "http://" + eurekaContainer.getHost() + ":8761/eureka/")
    //         .withEnv("server.port", "8080")
    //         .withNetwork(network)
    //         .dependsOn(eurekaContainer)
    //         .waitingFor(Wait.forLogMessage(
    //                 ".*- registration status: 204.*", 1))
    //         .withExposedPorts(8080);

    // private static WireMockServer footballMockServer;
    // private static WireMockServer eurekaMockServer;

    // @BeforeAll
    // static void init() {
    // footballMockServer = new WireMockServer(45459);
    // footballMockServer.start();
    // WireMock.configureFor(45459);

    // eurekaMockServer = new WireMockServer(8762);
    // eurekaMockServer.start();
    // WireMock.configureFor(8762);
    // }

    @Test
    public void testGetPlayers() throws Exception {
        // WireMock.stubFor(WireMock.get("/eureka/v2/apps/FootballServer")
        // .willReturn(WireMock.aResponse()
        // .withStatus(200)
        // .withHeader("Content-Type", "application/json")
        // .withBodyFile("eureka-FootballServer.json")));

        // WireMock.stubFor(WireMock.get("/eureka/v2/apps")
        // .willReturn(WireMock.aResponse()
        // .withStatus(200)
        // .withHeader("Content-Type", "application/json")
        // .withBodyFile("eureka-FootballServer.json")));

        // WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/players"))
        // .willReturn(WireMock.aResponse()
        // .withHeader("Content-Type", "application/json")
        // .withBody("""
        // [
        // {
        // "id": "325636",
        // "jerseyNumber": 11,
        // "name": "Alexia PUTELLAS",
        // "position": "Midfielder",
        // "dateOfBirth": "1994-02-04"
        // },
        // {
        // "id": "396930",
        // "jerseyNumber": 2,
        // "name": "Ona BATLLE",
        // "position": "Defender",
        // "dateOfBirth": "1999-06-10"
        // }
        // ]""")));
        // int port = footballContainer.getMappedPort(8080);
        // assert port > 0;

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/albums/players"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name")
                        .value("Alexia PUTELLAS"));
    }

}
