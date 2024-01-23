package com.packt.football;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testListPlayers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/players").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
              .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("Ivana ANDRES"))
              .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value("Alexia PUTELLAS"));
    }

    @Test
    public void testReadPlayer() throws Exception {
        String name = "Ivana ANDRES";
        mvc.perform(MockMvcRequestBuilders.get("/players/" + name).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(name));
    }

    @Test
    public void testDeletePlayer() throws Exception {
        String name = "Ivana ANDRES";
        mvc.perform(MockMvcRequestBuilders.delete("/players/" + name).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Player " + name + " deleted"));
    }

    @Test
    public void testUpdatePlayer() throws Exception {
        String name = "Ivana ANDRES";
        String newName = "Ivana ANDRES SANCHEZ";
        mvc.perform(MockMvcRequestBuilders.put("/players/" + name).content(newName).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Player " + name + " updated to " + newName));
    }
    
}
