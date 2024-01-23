package com.packt.albums;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AlbumsConfiguration {

    @Value("${football.api.url:http://localhost:8080}")
    String baseURI;

    @Bean
    RestClient restClient() {
        return RestClient.create(baseURI);
    }
}
