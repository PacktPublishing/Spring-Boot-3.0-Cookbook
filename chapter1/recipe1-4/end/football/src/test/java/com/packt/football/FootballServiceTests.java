package com.packt.football;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.packt.football.exceptions.AlreadyExistsException;
import com.packt.football.exceptions.NotFoundException;
import com.packt.football.model.Player;
import com.packt.football.services.FootballService;

@SpringBootTest
public class FootballServiceTests {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public FootballService footballService() {
            return new FootballService();
        }
    }

    @Autowired
    private FootballService footballService;

    @Test
    public void testListPlayers() {
        List<Player> players = footballService.listPlayers();
        assertFalse(players.isEmpty());
    }

    @Test
    public void testGetPlayer_exist() {
        Player player = footballService.getPlayer("1884823");
        assertNotNull(player);
    }

    @Test
    public void testGetPlayer_notExist() {
        assertThrows(NotFoundException.class, () -> footballService.getPlayer("9999999"));
    }

    @Test
    public void testAddPlayer_ok() {
        Player player = new Player("888888", 99, "Test Player", "Test Position", null);
        footballService.addPlayer(player);
        Player player2 = footballService.getPlayer("888888");
        assertNotNull(player2);
        assertEquals(player, player2);
    }

    @Test
    public void testAddPlayer_duplicate() {
        Player player = new Player("777777", 99, "Test Player", "Test Position", null);
        footballService.addPlayer(player);
        Player player2 = footballService.getPlayer("777777");
        assertNotNull(player2);
        assertEquals(player, player2);
        assertThrows(AlreadyExistsException.class, () -> footballService.addPlayer(player));
    }

    @Test
    public void testUpdatePlayer_exists() {
        // ARRANGE
        Player player = new Player("666666", 99, "Test Player", "Test Position", null);
        footballService.addPlayer(player);
        Player player2 = footballService.getPlayer("666666");
        assertNotNull(player2);
        assertEquals(player, player2);
        // ACT
        Player player3 = new Player("666666", 99, "Test Player 2", "Test Position 2", null);
        footballService.updatePlayer(player3);
        // ASSERT
        Player player4 = footballService.getPlayer("666666");
        assertNotNull(player4);
        assertEquals(player3, player4);
    }

    @Test
    public void testUpdatePlayer_notExists() {
        // ACT & ASSERT
        Player player = new Player("55555555", 99, "Test Player 2", "Test Position 2", null);
        assertThrows(NotFoundException.class, () -> footballService.updatePlayer(player));

    }

}
