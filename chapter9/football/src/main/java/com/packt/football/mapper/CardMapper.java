package com.packt.football.mapper;


import org.springframework.stereotype.Component;

import com.packt.football.domain.Card;
import com.packt.football.repo.CardEntity;

@Component
public class CardMapper {
    private PlayerMapper playerMapper;

    public CardMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    public Card map(CardEntity cardEntity) {
        return new Card(cardEntity.getId(),
                cardEntity.getOwner().getId(),
                cardEntity.getAlbum() != null ? cardEntity.getAlbum().getId() : null,
                playerMapper.map(cardEntity.getPlayer()));
    }
}
