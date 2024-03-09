package com.packt.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ConsumerApplication.class,
        ConsumerController.class, ConsumerControllerTests.Config.class})
public class ConsumerControllerTests {

    @TestConfiguration
    static class Config {
        @Bean
        public WireMockServer webServer() {
            WireMockServer wireMockServer = new WireMockServer(7979);
            wireMockServer.start();
            return wireMockServer;
        }
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("footballservice.url", () -> "http://localhost:7979");
    }

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private WireMockServer server;

    @Test
    public void getCards() {
        // ARRANGE
        server.stubFor(WireMock.get(WireMock.urlEqualTo("/cards"))
                .willReturn(
                        WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                [
                                    {
                                        "cardId": "1",
                                        "album": "WWC23",
                                        "player": "Ivana Andres",
                                        "ranking": 7
                                    },
                                    {
                                        "cardId": "2",
                                        "album": "WWC23",
                                        "player": "Alexia Putellas",
                                        "ranking": 1
                                    }
                                ]""")));
        // ACT & ASSERT
        webTestClient.get().uri("/consumer/cards")
                .exchange().expectStatus().isOk()
                .expectBodyList(Card.class).hasSize(2)
                .contains(new Card("1", "WWC23", "Ivana Andres", 7),
                        new Card("2", "WWC23", "Alexia Putellas", 1));
    }

    @Test
    public void getCard_found() {
        // ARRANGE
        server.stubFor(WireMock.get(WireMock.urlEqualTo("/cards/1")).willReturn(
                WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody("""
                        {
                            "cardId": "1",
                            "album": "WWC23",
                            "player": "Ivana Andres",
                            "ranking": 7
                        }""")));
        // ACT & ASSERT
        webTestClient.get().uri("/consumer/cards/1").exchange().expectStatus().isOk().expectBody(Card.class)
                .isEqualTo(new Card("1", "WWC23", "Ivana Andres", 7));

    }

    @Test
    public void getCard_notFound() {
        // ARRANGE
        server.stubFor(WireMock.get(WireMock.urlEqualTo("/cards/1")).willReturn(WireMock.aResponse().withStatus(404)));
        // ACT & ASSERT
        webTestClient.get().uri("/consumer/cards/1").exchange().expectStatus().isOk().expectBody().isEmpty();
    }

    @Test
    public void getFailedRequest_remoteError() {
        // ARRANGE
        server.stubFor(WireMock.get(WireMock.urlEqualTo("/invalidpath")).willReturn(
                WireMock.aResponse().withStatus(500).withHeader("Content-Type", "application/json").withBody("""
                        {
                            "timestamp": "2021-08-01T12:00:00.000+00:00",
                            "status": 500,
                            "error": "Internal Server Error",
                            "path": "/invalidpath",
                            "message": "Something went wrong"
                        }""")));
        // ACT & ASSERT
        String content = webTestClient.get().uri("/consumer/error").exchange().expectStatus().isOk()
                .returnResult(String.class).getResponseBody().blockFirst();
        assert (content.contains("Remote server returned 500"));
    }

    @Test
    public void getFailedRequest_remoteNotFound() {
        // ARRANGE
        server.stubFor(WireMock.get(WireMock.urlEqualTo("/invalidpath")).willReturn(
                WireMock.aResponse().withStatus(404).withHeader("Content-Type", "application/json").withBody("""
                        {
                            "timestamp": "2021-08-01T12:00:00.000+00:00",
                            "status": 404,
                            "error": "Not Found Error",
                            "path": "/invalidpath",
                            "message": "Not found"
                        }""")));
        // ACT & ASSERT
        webTestClient.get().uri("/consumer/error").exchange().expectStatus().isOk().expectBody(String.class)
                .isEqualTo("Remote server returned 404");
    }
}
