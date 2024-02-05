package com.packt.footballpg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.packt.footballpg.service.TeamsService;

@SpringBootTest
public class TeamsServiceTests {

    @Autowired
    private TeamsService teamsService;

    @Test
    public void testGetTeamCount() {
        int count = teamsService.getTeamCount();
        assertTrue(count > 0);
    }

    @Test
    public void testGetTeams() {
        assertTrue(teamsService.getTeams().size() > 0);
    }

    @Test
    public void testGetTeam() {
        assertEquals("Argentina", teamsService.getTeam(1884881).getName());
    }
}
