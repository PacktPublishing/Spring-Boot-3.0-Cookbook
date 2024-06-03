package com.packt.footballmdb.service;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.packt.footballmdb.repository.Card;
import com.packt.footballmdb.repository.CardRepository;
import com.packt.footballmdb.repository.User;
import com.packt.footballmdb.repository.UserRepository;

@Service
public class TradingService {
    private CardRepository cardRepository;
    private UserRepository userRepository;

    public TradingService(CardRepository cardRepository,
                          UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public boolean exchangeCard(String cardId, String newOwnerId, Integer price) {
        try {
            exchangeCardInternal(cardId, newOwnerId, price);
            return true;
        } catch (OptimisticLockingFailureException e) {
            return false;
        }
    }

    @Transactional
    private void exchangeCardInternal(String cardId, String newOwnerId, Integer price) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        User newOwner = userRepository.findById(newOwnerId).orElseThrow();
        if (newOwner.getTokens() < price) {
            throw new RuntimeException("Not enough tokens");
        }
        newOwner.setTokens(newOwner.getTokens() - price);
        User oldOwner = card.getOwner();
        oldOwner.setTokens(oldOwner.getTokens() + price);
        card.setOwner(newOwner);
        card = cardRepository.save(card);
        userRepository.saveAll(List.of(newOwner, oldOwner));
    }

}
