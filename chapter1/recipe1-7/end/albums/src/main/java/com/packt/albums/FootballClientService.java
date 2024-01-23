package com.packt.albums;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FootballClientService {

    private RestClient restClient;

    public FootballClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Player> getPlayers() {
        return restClient.get().uri("/players").retrieve()
                .body(new ParameterizedTypeReference<List<Player>>() {
                });
    }

    public Optional<Player> getPlayer(String id) {
        return restClient.get().uri("/players/{id}", id)
                .exchange((request, response) -> {
                    if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Optional.empty();
                    }
                    return Optional.of(response.bodyTo(Player.class));
                });
    }
}
