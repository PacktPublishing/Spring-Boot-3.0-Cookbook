package com.packt.football.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.packt.football.exceptions.AlreadyExistsException;
import com.packt.football.exceptions.NotFoundException;
import com.packt.football.model.Player;

@Service
public class FootballService {
    private final Map<String, Player> players = Map.ofEntries(
            Map.entry("1884823", new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 07, 13))),
            Map.entry("325636", new Player("325636", 11, "Alexia PUTELLAS", "Midfielder", LocalDate.of(1994, 02, 04))),
            Map.entry("396930", new Player("396930", 2, "Ona BATLLE", "Defender", LocalDate.of(1999, 06, 10))),
            Map.entry("377762", new Player("377762", 6, "Aitana BONMATI", "Midfielder", LocalDate.of(1998, 01, 18))),
            Map.entry("387133", new Player("387133", 4, "Irene PAREDES", "Defender", LocalDate.of(1998, 01, 18))),
            Map.entry("387134", new Player("387134", 8, "Vicky LOSADA", "Midfielder", LocalDate.of(1991, 01, 01))),
            Map.entry("396911", new Player("396911", 15, "Eva NAVARRO", "Forward", LocalDate.of(2001, 01, 27))),
            Map.entry("420276", new Player("420276", 9, "Esther GONZALEZ", "Forward", LocalDate.of(1992, 12, 8))),
            Map.entry("377723", new Player("377723", 20, "Rocio GALVEZ", "Defender", LocalDate.of(1997, 04, 15))),
            Map.entry("396914", new Player("396914", 14, "Laia CODINA", "Defender", LocalDate.of(2000, 01, 22))),
            Map.entry("420284", new Player("420284", 21, "Claudia ZORNOZA", "Midfielder", LocalDate.of(1990, 10, 20))),
            Map.entry("415394", new Player("415394", 18, "Salma PARALLUELO", "Forward", LocalDate.of(2003, 11, 13))),
            Map.entry("387138", new Player("387138", 10, "Jennifer HERMOSO", "Forward", LocalDate.of(1990, 05, 9))),
            Map.entry("467287", new Player("467287", 16, "Maria PEREZ", "Midfielder", LocalDate.of(2001, 12, 24))),
            Map.entry("420277", new Player("420277", 7, "Irene GUERRERO", "Midfielder", LocalDate.of(1996, 12, 12))),
            Map.entry("396929", new Player("396929", 12, "Oihane HERNANDEZ", "Defender", LocalDate.of(2000, 05, 04))),
            Map.entry("396907", new Player("396907", 23, "Cata COLL", "Goalkeeper", LocalDate.of(2001, 04, 23))),
            Map.entry("467297",
                    new Player("467297", 22, "Athenea DEL CASTILLO", "Forward", LocalDate.of(2000, 10, 24))),
            Map.entry("398097", new Player("398097", 8, "Mariona CALDENTEY", "Forward", LocalDate.of(1996, 03, 19))),
            Map.entry("398098", new Player("398098", 1, "Misa RODRIGUEZ", "Goalkeeper", LocalDate.of(1999, 07, 22))),
            Map.entry("413016", new Player("413016", 19, "Olga CARMONA", "Defender", LocalDate.of(2000, 06, 12))),
            Map.entry("398088", new Player("398088", 17, "Alba REDONDO", "Forward", LocalDate.of(1996, 8, 27))),
            Map.entry("413022", new Player("413022", 3, "Teresa ABELLEIRA", "Midfielder", LocalDate.of(2000, 01, 9))),
            Map.entry("415396", new Player("415396", 13, "Enith SALON", "Goalkeeper", LocalDate.of(2001, 9, 24))));

    public List<Player> listPlayers() {
        return players.values().stream().collect(Collectors.toList());
    }

    public Player getPlayer(String id) {
        Player player = players.get(id);
        if (player == null)
            throw new NotFoundException("Player not found");
        return player;
    }

    public Player addPlayer(Player player) {
        if (players.containsKey(player.id())) {
            throw new AlreadyExistsException("The player already exists");
        } else {
            players.put(player.id(), player);
            return player;
        }
    }

    public Player updatePlayer(Player player) {
        if (!players.containsKey(player.id())) {
            throw new NotFoundException("The player does not exist");
        } else {
            players.put(player.id(), player);
            return player;
        }
    }

    public void deletePlayer(String id) {
        if (players.containsKey(id)) {
            players.remove(id);
        }
    }





}
