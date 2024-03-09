package com.packt.football.mapper;

import com.packt.football.domain.Album;
import com.packt.football.repo.AlbumEntity;

public class AlbumMapper {
    public static Album map(AlbumEntity albumEntity) {
        return new Album(albumEntity.getId(), albumEntity.getTitle());
    }
}
