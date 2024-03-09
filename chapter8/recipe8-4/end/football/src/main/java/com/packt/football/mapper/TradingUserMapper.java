package com.packt.football.mapper;

import com.packt.football.domain.TradingUser;
import com.packt.football.domain.User;
import com.packt.football.repo.UserEntity;
import com.packt.football.repo.UserMapper;

public class TradingUserMapper {
    public static TradingUser map(UserEntity userEntity) {
        return new TradingUser(UserMapper.map(userEntity),
                userEntity.getOwnedCards().stream().map(CardMapper::map).toList(),
                userEntity.getOwnedAlbums().stream().map(AlbumMapper::map).toList());

    }
}
