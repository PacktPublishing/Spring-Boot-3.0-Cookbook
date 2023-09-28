package com.packt.cardsfunctional;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CardsRouterConfig {
    @Bean
    CardsHandler cardsHandler() {
        return new CardsHandler();
    }

    @Bean
    RouterFunction<ServerResponse> getCards() {
        return route(GET("/cards"), req -> ok().body(cardsHandler().getCards(), Card.class));
    }

    @Bean
    RouterFunction<ServerResponse> getCard(){
        return route(GET("/cards/{cardId}"), req -> ok().body(cardsHandler().getCard(req.pathVariable("cardId")), Card.class));
    }
}
