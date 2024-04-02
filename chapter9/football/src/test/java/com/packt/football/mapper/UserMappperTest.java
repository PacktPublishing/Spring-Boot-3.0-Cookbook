package com.packt.football.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.packt.football.domain.TradingUser;
import com.packt.football.repo.AlbumEntity;
import com.packt.football.repo.CardEntity;
import com.packt.football.repo.PlayerEntity;
import com.packt.football.repo.UserEntity;

class UserMappperTest {

    @Test
    void mapNoAlbumsNoCards() {
        // given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("user1");
        userEntity.setOwnedAlbums(null);
        userEntity.setOwnedCards(null);

        // when
        UserMappper userMappper = new UserMappper(new CardMapper(new PlayerMapper()));
        TradingUser tradingUser = userMappper.map(userEntity);

        // then
        assertNotNull(tradingUser);
        assertEquals(1, tradingUser.getUser().getId());
        assertEquals("user1", tradingUser.getUser().getName());
        assertNull(tradingUser.getAlbums());
        assertNull(tradingUser.getCards());
    }

    @Test
    void mapWithAlbumsAndCards() {
        // given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("user1");
        AlbumEntity album1 = new AlbumEntity();
        album1.setId(1);
        album1.setTitle("album1");
        AlbumEntity album2 = new AlbumEntity();
        album2.setId(2);
        album2.setTitle("album2");
        userEntity.setOwnedAlbums(Set.of(album1, album2));
        CardEntity card1 = new CardEntity();
        card1.setId(1);
        card1.setOwner(userEntity);
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1);
        player1.setName("player1");
        card1.setPlayer(player1);

        userEntity.setOwnedCards(List.of(card1));

        // when
        UserMappper userMappper = new UserMappper(new CardMapper(new PlayerMapper()));
        TradingUser tradingUser = userMappper.map(userEntity);

        // then
        assertNotNull(tradingUser);
        assertEquals(1, tradingUser.getUser().getId());
        assertEquals("user1", tradingUser.getUser().getName());
        assertNotNull(tradingUser.getAlbums());
        assertThat(tradingUser.getAlbums()).hasSize(2);
        assertNotNull(tradingUser.getCards());
        assertThat(tradingUser.getCards()).hasSize(1);
    }

}