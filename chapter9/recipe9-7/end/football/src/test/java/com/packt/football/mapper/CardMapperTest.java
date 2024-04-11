package com.packt.football.mapper;

import com.packt.football.domain.Card;
import com.packt.football.domain.Player;
import com.packt.football.repo.AlbumEntity;
import com.packt.football.repo.CardEntity;
import com.packt.football.repo.PlayerEntity;
import com.packt.football.repo.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardMapperTest {

    @Test
    void map() {
        // given
        CardMapper mapper = new CardMapper(new PlayerMapper());


        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("user1");
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(1);
        playerEntity.setName("player1");
        playerEntity.setPosition("Midfielder");
        playerEntity.setJerseyNumber(6);
        playerEntity.setDateOfBirth(LocalDate.of(1998, 1, 18));

        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setId(1);
        albumEntity.setTitle("album1");
        albumEntity.setOwner(userEntity);

        CardEntity cardEntity = new CardEntity();
        cardEntity.setId(1);
        cardEntity.setPlayer(playerEntity);
        cardEntity.setAlbum(albumEntity);
        cardEntity.setOwner(userEntity);

        // when
        Card card = mapper.map(cardEntity);

        // then
        assertNotNull(card);
        assertEquals(1, card.getId());
        assertEquals(1, card.getOwnerId());
        assertEquals(1, card.getAlbumId());
        Player player = card.getPlayer();
        assertNotNull(player);
        assertEquals(1, player.getId());
        assertEquals("player1", player.getName());
        assertEquals("Midfielder", player.getPosition());
        assertEquals(6, player.getJerseyNumber());

    }
}