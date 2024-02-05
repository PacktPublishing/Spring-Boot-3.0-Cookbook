package com.packt.footballpg;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.packt.footballpg.entities.Player;
import com.packt.footballpg.service.PlayersService;

@SpringBootTest
public class PlayersServiceTests {

    @Autowired
    private PlayersService playersService;

    @Test
    public void testGetPlayer() {
        Player player = playersService.getPlayer(387138);
        assertNotNull(player);
    }

    @Test
    public void testGetPlayers() {
        List<Player> players = playersService.getPlayers();
        assertNotNull(players);
        assertTrue(players.size() > 0);
    }

    @Test
    public void testAddPlayers() {
        Random random = new Random();
        Player player = new Player();
        player.setName("Random player " + random.nextInt());
        player.setJerseyNumber(random.nextInt(20));
        player.setPosition("Midfielder");
        player.setTeamId(1884823);

        Player createdPlayer = playersService.createPlayer(player);
        assertNotNull(createdPlayer);
        assertNotNull(createdPlayer.getId());
        assertTrue(createdPlayer.getId() > 0);

        List<Player> players = playersService.getPlayers();
        assertTrue(players.stream().filter(t -> t.getName().contains(player.getName())).findAny().isPresent());
    }

}
