package com.packt.albums;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AlbumsControllerTests {

    @Autowired
    MockMvc mvc;

    private static WireMockServer wireMockServer;
    @BeforeAll
    static void init(){
        wireMockServer = new WireMockServer(7979);
        wireMockServer.start();
        WireMock.configureFor(7979);
    }

    @Test
    public void testGetPlayers() throws Exception {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/players"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        [
                            {
                                "id": "325636",
                                "jerseyNumber": 11,
                                "name": "Alexia PUTELLAS",
                                "position": "Midfielder",
                                "dateOfBirth": "1994-02-04"
                            },
                            {
                                "id": "396930",
                                "jerseyNumber": 2,
                                "name": "Ona BATLLE",
                                "position": "Defender",
                                "dateOfBirth": "1999-06-10"
                            }
                        ]""")));

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/albums/players"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name").value("Alexia PUTELLAS"));
    }

}
