package com.packt.footballpg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.packt.footballpg.service.FootballQueryService;

@SpringBootTest
public class FootballServiceQueryTests {

    @Autowired
    private FootballQueryService footballQueryService;

    @Test
    public void testGetTeamCount() {
        int count = footballQueryService.getTeamCount();
        assertTrue(count > 0);
    }

    @Test
    public void testGetTeams() {
        assertTrue(footballQueryService.getTeams().size() > 0);
    }

    @Test
    public void testGetTeam() {
        assertEquals("Argentina", footballQueryService.getTeam(1884881).getName());
    }
}
