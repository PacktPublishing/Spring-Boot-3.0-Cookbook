package com.packt.football;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/players")
@RestController
public class PlayerController {
    @GetMapping
    public List<String> listPlayers() {
        return List.of("Ivana ANDRES", "Alexia PUTELLAS");
    }

    @PostMapping
    public String createPlayer(@RequestBody String name) {
        return "Player " + name + " created";
    }

    @GetMapping("/{name}")
    public String readPlayer(@PathVariable String name) {
        return name;
    }

    @DeleteMapping("/{name}")
    public String deletePlayer(@PathVariable String name) {
        return "Player " + name + " deleted";
    }

    @PutMapping("/{name}")
    public String updatePlayer(@PathVariable String name, @RequestBody String newName) {
        return "Player " + name + " updated to " + newName;
    }
}
