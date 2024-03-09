package com.packt.football.mapper;

import com.packt.football.domain.Card;
import com.packt.football.repo.CardEntity;

import java.util.Optional;

public class CardMapper {
    public static Card map(CardEntity cardEntity) {
        return new Card(cardEntity.getId(),
                cardEntity.getAlbum() != null ? Optional.of(AlbumMapper.map(cardEntity.getAlbum())) : Optional.empty(),
                PlayerMapper.map(cardEntity.getPlayer()));
    }
}
