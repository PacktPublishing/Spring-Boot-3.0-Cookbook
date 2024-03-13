package com.packt.football.mapper;

import com.packt.football.domain.Card;
import com.packt.football.repo.AlbumEntity;
import com.packt.football.repo.CardEntity;
import com.packt.football.repo.PlayerEntity;

import java.util.Optional;

public class CardMapper {
    public static Card map(CardEntity cardEntity, Optional<AlbumEntity> albumEntity, PlayerEntity playerEntity) {
        return new Card(cardEntity.getId(), albumEntity.map(AlbumMapper::map), PlayerMapper.map(playerEntity));
    }
}
