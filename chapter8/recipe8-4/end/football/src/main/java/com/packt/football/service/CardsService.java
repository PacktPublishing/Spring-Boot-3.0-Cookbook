package com.packt.football.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.packt.football.domain.Card;
import com.packt.football.mapper.CardMapper;
import com.packt.football.repo.AlbumEntity;
import com.packt.football.repo.AlbumsRepository;
import com.packt.football.repo.CardEntity;
import com.packt.football.repo.CardsRepository;
import com.packt.football.repo.PlayerEntity;
import com.packt.football.repo.PlayersRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardsService {

    private final CardsRepository cardsRepository;
    private final PlayersRepository playersRepository;
    private final AlbumsRepository albumsRepository;

    public CardsService(CardsRepository cardRepository, PlayersRepository playersRepository, AlbumsRepository albumsRepository) {
        this.cardsRepository = cardRepository;
        this.playersRepository = playersRepository;
        this.albumsRepository = albumsRepository;
    }

    public Mono<Card> getCard(Long cardId) {
        return cardsRepository.findById(cardId)
                .flatMap(this::retrieveRelations)
                .switchIfEmpty(Mono.empty());
    }

    public Flux<Card> getCards() {
        return cardsRepository.findAll()
                .flatMap(this::retrieveRelations)
                .onErrorResume(e -> {
                    System.err.println("Error: " + e.getMessage());
                    return Flux.empty();
                });
    }

    public Mono<Card> createCard(Long playerId) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setPlayerId(playerId);
        return cardsRepository.save(cardEntity)
                .flatMap(c ->
                        this.playersRepository.findById(playerId)
                                .map(player -> CardMapper.map(c, Optional.empty(), player)));
    }

    protected Mono<Card> retrieveRelations(CardEntity cardEntity) {
        Mono<PlayerEntity> playerEntityMono = playersRepository.findById(cardEntity.getPlayerId());
        Mono<Optional<AlbumEntity>> albumEntityMono;
        if (cardEntity.getAlbumId() != null
                && cardEntity.getAlbumId().isPresent()) {
            albumEntityMono = albumsRepository.findById(
                            cardEntity.getAlbumId().get())
                    .map(Optional::of);
        } else {
            albumEntityMono = Mono.just(Optional.empty());
        }
        return Mono.zip(playerEntityMono, albumEntityMono)
                .map(tuple ->
                        CardMapper.map(cardEntity,
                                tuple.getT2(), tuple.getT1()));
    }
}
