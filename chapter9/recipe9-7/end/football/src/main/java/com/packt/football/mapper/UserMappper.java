package com.packt.football.mapper;

import java.util.stream.Collectors;

import com.packt.football.domain.Album;
import com.packt.football.domain.TradingUser;
import com.packt.football.domain.User;
import com.packt.football.repo.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMappper {
    private final CardMapper cardMapper;

    public UserMappper(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    public TradingUser map(UserEntity userEntity) {
        return new TradingUser(new User(userEntity.getId(), userEntity.getUsername()),
                userEntity.getOwnedCards() == null ? null
                        : userEntity.getOwnedCards()
                                .stream()
                                .map(cardMapper::map)
                                .collect(Collectors.toList()),
                userEntity.getOwnedAlbums() == null ? null
                        : userEntity.getOwnedAlbums()
                                .stream()
                                .map(a -> new Album(a.getId(), a.getTitle(), userEntity.getId()))
                                .collect(Collectors.toList()));

    }

}
