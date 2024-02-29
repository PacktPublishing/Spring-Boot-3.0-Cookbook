package com.packt.football.service;

import com.packt.football.domain.Album;
import com.packt.football.domain.Card;
import com.packt.football.domain.TradingUser;
import com.packt.football.domain.User;
import com.packt.football.mapper.PlayerMapper;
import com.packt.football.repo.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AlbumsService {
    private final AlbumRepository albumsRepository;
    private final UserRepository usersRepository;
    private final PlayerRepository playersRepository;
    private final CardRepository cardsRepository;
    private final PlayerMapper playerMapper;

    public AlbumsService(AlbumRepository albumsRepository, UserRepository usersRepository,
            PlayerRepository playersRepository, CardRepository cardsRepository, PlayerMapper playerMapper) {
        this.albumsRepository = albumsRepository;
        this.usersRepository = usersRepository;
        this.playersRepository = playersRepository;
        this.cardsRepository = cardsRepository;
        this.playerMapper = playerMapper;
    }

    @Transactional
    public Album buyAlbum(Integer userId, String title) {
        AlbumEntity album = new AlbumEntity();
        album.setTitle(title);
        album.setExpireDate(LocalDate.now().plusYears(1));
        album.setOwner(usersRepository.findById(userId).orElseThrow());
        album = albumsRepository.save(album);
        return new Album(album.getId(), album.getTitle(), album.getOwner().getId());
    }

    @Transactional
    public List<Card> buyCards(Integer userId, Integer count) {
        Random rnd = new Random();
        List<PlayerEntity> players = getAvailablePlayers();
        UserEntity owner = usersRepository.findById(userId).orElseThrow();
        List<CardEntity> cards = Stream.generate(() -> {
            CardEntity card = new CardEntity();
            card.setOwner(owner);
            card.setPlayer(players.get(rnd.nextInt(players.size())));
            return card;
        }).limit(count).toList();
        return cardsRepository.saveAll(cards)
                .stream()
                .map(card -> new Card(card.getId(), card.getOwner().getId(), Optional.empty(),
                        playerMapper.map(card.getPlayer())))
                .collect(Collectors.toList());
    }

    public Card addCardToAlbum(Integer cardId, Integer albumId) {
        CardEntity card = cardsRepository.findById(cardId).orElseThrow();
        AlbumEntity album = albumsRepository.findById(albumId).orElseThrow();
        card.setAlbum(album);
        card = cardsRepository.save(card);
        return new Card(card.getId(), card.getOwner().getId(), Optional.of(card.getAlbum().getId()),
                playerMapper.map(card.getPlayer()));

    }

    /*
     * Take all non used cards of the user and assign to the albums of the user
     */
    @Transactional
    public List<Card> useAllCardAvailable(Integer userId) {
        // UserEntity user = usersRepository.findById(userId).orElseThrow();
        // List<CardEntity> cards = cardsRepository.findAllByOwnerAndAlbumIsNull(user);
        // List<AlbumEntity> albums = albumsRepository.findAllByOwner(user);
        // for (int i = 0; i < cards.size(); i++) {
        // cards.get(i).setAlbum(albums.get(i % albums.size()));
        // }
        // cardsRepository.saveAll(cards);
        return cardsRepository.assignCardsToUserAlbums(userId)
                .stream()
                .map(c -> new Card(c.getId(), c.getOwner().getId(),
                        c.getAlbum() == null ? Optional.empty() : Optional.of(c.getAlbum().getId()),
                        playerMapper.map(c.getPlayer())))
                .toList();

    }

    @Transactional
    public Optional<Card> transferCard(Integer cardId, Integer userId) {
        Integer count = cardsRepository.transferCard(cardId, userId);
        if (count == 0) {
            return Optional.empty();
        } else {
            CardEntity card = cardsRepository.findById(cardId).orElseThrow();
            if (card.getOwner()==null) {
                throw new RuntimeException("New owner not found");
            }
            return Optional.of(new Card(card.getId(), card.getOwner().getId(),
                    card.getAlbum() == null ? Optional.empty() : Optional.of(card.getAlbum().getId()),
                    playerMapper.map(card.getPlayer())));
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Card> tradeAllCards(Integer userId1, Integer userId2) {
        Integer potentialUser1ToUser2 = cardsRepository.countMatchBetweenUsers(userId1, userId2);
        Integer potentialUser2ToUser1 = cardsRepository.countMatchBetweenUsers(userId2, userId1);
        Integer count = Math.min(potentialUser1ToUser2, potentialUser2ToUser1);
        if (count > 0) {
            ArrayList<CardEntity> result1 = new ArrayList<>(
                    cardsRepository.tradeCardsBetweenUsers(userId1, userId2, count));
            useAllCardAvailable(userId2);
            ArrayList<CardEntity> result2 = new ArrayList<>(
                    cardsRepository.tradeCardsBetweenUsers(userId2, userId1, count));
            useAllCardAvailable(userId1);
            return Stream.concat(result1.stream(), result2.stream())
                    .map(c -> new Card(c.getId(), c.getOwner().getId(),
                            c.getAlbum() == null ? Optional.empty() : Optional.of(c.getAlbum().getId()),
                            playerMapper.map(c.getPlayer())))
                    .toList();
        } else {
            return List.of();
        }
    }

    @Cacheable(value = "availablePlayers")
    private List<PlayerEntity> getAvailablePlayers() {
        return playersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TradingUser> getUserWithCardsAndAlbums(Integer userId) {
        Optional<UserEntity> user = usersRepository.findByIdWithCardsAndAlbums(userId);
        if (user.isPresent()) {
            UserEntity u = user.get();
            return Optional.of(new TradingUser(new User(u.getId(), u.getUsername()),
                    u.getOwnedCards()
                            .stream()
                            .map(c -> new Card(c.getId(), u.getId(),
                                    c.getAlbum() == null ? Optional.empty() : Optional.of(c.getAlbum().getId()),
                                    playerMapper.map(c.getPlayer())))
                            .toList(),
                    u.getOwnedAlbums()
                            .stream()
                            .map(a -> new Album(a.getId(), a.getTitle(), u.getId()))
                            .toList()));
        } else {
            return Optional.empty();
        }
    }
}
