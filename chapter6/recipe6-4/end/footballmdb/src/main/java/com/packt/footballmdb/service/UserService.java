package com.packt.footballmdb.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.result.UpdateResult;
import com.packt.footballmdb.repository.Card;
import com.packt.footballmdb.repository.CardRepository;
import com.packt.footballmdb.repository.Player;
import com.packt.footballmdb.repository.PlayerRepository;
import com.packt.footballmdb.repository.User;
import com.packt.footballmdb.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private PlayerRepository playersRepository;
    private CardRepository cardsRepository;
    private MongoTemplate mongoTemplate;

    public UserService(UserRepository userRepository, PlayerRepository playersRepository,
            CardRepository cardsRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.playersRepository = playersRepository;
        this.cardsRepository = cardsRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Integer buyTokens(String userId, Integer tokens) {
        Query query = new Query(Criteria.where("id").is(userId));
        Update update = new Update().inc("tokens", tokens);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class,
                "users");
        return (int) result.getModifiedCount();
    }

    @Transactional
    public Integer buyCards(String userId, Integer count) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Player> availablePlayers = getAvailablePlayers();
            Random random = new Random();
            if (user.getTokens() >= count) {
                user.setTokens(user.getTokens() - count);
            } else {
                throw new RuntimeException("Not enough tokens");
            }
            List<Card> cards = Stream.generate(() -> {
                Card card = new Card();
                card.setOwner(user);
                card.setPlayer(availablePlayers.get(random.nextInt(0, availablePlayers.size())));
                return card;
            }).limit(count).toList();
            List<Card> savedCards = cardsRepository.saveAll(cards);
            userRepository.save(user);
            return savedCards.size();
        }
        return 0;
    }

    @Cacheable(value = "availablePlayers")
    private List<Player> getAvailablePlayers() {
        return playersRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

}
