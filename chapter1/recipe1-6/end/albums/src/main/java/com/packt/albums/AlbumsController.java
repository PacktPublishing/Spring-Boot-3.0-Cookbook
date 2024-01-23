package com.packt.albums;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albums")
public class AlbumsController {
    private final FootballClient footballClient;

    public AlbumsController(FootballClient footballClient) {
        this.footballClient = footballClient;
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return footballClient.getPlayers();
    }

}
