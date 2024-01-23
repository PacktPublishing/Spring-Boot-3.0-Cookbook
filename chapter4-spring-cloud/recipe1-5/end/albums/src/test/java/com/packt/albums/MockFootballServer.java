package com.packt.albums;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@TestConfiguration
@RestController
@ActiveProfiles("mock-football")
public class MockFootballServer {
    
    @GetMapping("/players")
    public String getPlayers() {
        return """
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
            ]""";
    }
    
}
