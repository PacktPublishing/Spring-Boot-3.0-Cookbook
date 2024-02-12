package com.packt.footballmdb.service;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.packt.footballmdb.repository.Match;
import com.packt.footballmdb.repository.MatchEvent;
import com.packt.footballmdb.repository.MatchEventRepository;
import com.packt.footballmdb.repository.MatchRepository;
import com.packt.footballmdb.repository.Player;
import com.packt.footballmdb.repository.PlayerRepository;
import com.packt.footballmdb.repository.Team;
import com.packt.footballmdb.repository.TeamRepository;

@Service
public class FootballService {

    private TeamRepository teamRepository;
    private MongoTemplate mongoTemplate;
    private PlayerRepository playerRepository;
    private MatchEventRepository matchEventRepository;
    private MatchRepository matchRepository;

    public FootballService(TeamRepository teamRepository, MongoTemplate mongoTemplate, PlayerRepository playerRepository, MatchEventRepository matchEventRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.mongoTemplate = mongoTemplate;
        this.playerRepository = playerRepository;
        this.matchEventRepository = matchEventRepository;
        this.matchRepository = matchRepository;
    }

    public Team getTeam(String id) {
        return teamRepository.findById(id).orElse(null);
    }

    public Team getTeamByName(String name) {
        return teamRepository.findByName(name).orElse(null);
    }

    public List<Team> getTeamsContainingName(String name) {
        return teamRepository.findByNameContaining(name);
    }

    public Player getPlayer(String id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public void deleteTeam(String id) {
        teamRepository.deleteById(id);
    }

    public void updateTeamName(String id, String name) {
        Query query = new Query(Criteria.where("id").is(id));
        Update updateName = new Update().set("name", name);
        mongoTemplate.updateFirst(query, updateName, Team.class);
    }

    public List<MatchEvent> getMatchEvents(String matchId) {
        return matchEventRepository.findByMatchId(matchId);
    }

    public List<MatchEvent> getPlayerEvents(String matchId, String playerId) {
        return matchEventRepository.findByMatchIdAndPlayerId(matchId, playerId);
    }

}
