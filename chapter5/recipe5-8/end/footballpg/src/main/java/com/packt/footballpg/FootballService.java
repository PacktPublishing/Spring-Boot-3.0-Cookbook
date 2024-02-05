package com.packt.footballpg;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FootballService {
        private PlayerRepository playerRepository;
        private TeamRepository teamRepository;
        private MatchRepository matchRepository;
        private AlbumRepository albumRepository;
        private MatchEventRepository matchEventRepository;

        public FootballService(PlayerRepository playerRepository,
                        TeamRepository teamRepository,
                        MatchRepository matchRepository,
                        AlbumRepository albumRepository,
                        MatchEventRepository matchEventRepository) {
                this.playerRepository = playerRepository;
                this.teamRepository = teamRepository;
                this.matchRepository = matchRepository;
                this.albumRepository = albumRepository;
                this.matchEventRepository = matchEventRepository;
        }

        // @Transactional(readOnly = true)
        public Team getTeam(Integer id) {
                TeamEntity team = teamRepository.findByIdWithPlayers(id).orElse(null);
                if (team == null) {
                        return null;
                } else {
                        return new Team(team.getId(),
                                        team.getName(),
                                        team.getPlayers()
                                                        .stream()
                                                        .map(player -> new Player(player.getName(),
                                                                        player.getJerseyNumber(), player.getPosition(),
                                                                        player.getDateOfBirth()))
                                                        .toList());
                }
        }

        public List<Player> searchPlayers(String name) {
                return playerRepository.findByNameContaining(name)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> searchPlayersStartingWith(String nameStarting) {
                return playerRepository.findByNameStartingWith(nameStarting)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> searchPlayersByBirthDate(LocalDate date) {
                return playerRepository.findByDateOfBirth(date)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public Team createTeam(String name) {
                TeamEntity team = new TeamEntity();
                team.setName(name);
                team = teamRepository.save(team);
                return new Team(team.getId(), team.getName(), List.of());
        }

        public Player updatePlayerPosition(Integer id, String position) {
                PlayerEntity player = playerRepository.findById(id).orElse(null);
                if (player == null) {
                        return null;
                } else {
                        player.setPosition(position);
                        player = playerRepository.save(player);
                        return new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
                                        player.getDateOfBirth());
                }
        }

        public List<Player> getPlayersByMatch(Integer id) {
                return matchRepository.findPlayersByMatchId(id)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> getAlbumMissingPlayers(Integer id) {
                return albumRepository.findByIdMissingPlayers(id)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> getAlbumPlayers(Integer id) {
                return albumRepository.findByIdPlayers(id, Pageable.ofSize(10).withPage(0))
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> getAlbumPlayersByTeam(Integer albumId, Integer teamId) {
                return albumRepository.findByIdAndTeam(albumId, teamId)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> getPlayersList(List<Integer> players) {
                // return playerRepository.findListOfPlayers(players)
                return playerRepository.findByIdIn(players)
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> searchPlayersLike(String q) {
                return playerRepository.findByNameLike("%" + q + "%")
                                .stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> getTeamPlayers(Integer id) {
                return playerRepository.findByTeamId(id, Sort.by("name").ascending()).stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<Player> getAllPlayersPaged(int pageNumber, int size) {
                Page<PlayerEntity> page = playerRepository.findAll(Pageable.ofSize(size).withPage(pageNumber));
                return page.stream()
                                .map(player -> new Player(player.getName(), player.getJerseyNumber(),
                                                player.getPosition(),
                                                player.getDateOfBirth()))
                                .toList();
        }

        public List<TeamPlayers> getNumberOfPlayersByPosition(String position) {
                return teamRepository.getNumberOfPlayersByPosition(position);
        }

        public Match getMatchWithTimeline(Integer matchId) {
                MatchEntity match = matchRepository.findByIdWithTimeline(matchId).orElse(null);
                if (match != null) {
                        return new Match(match.getId(), match.getTeam1().getName(), match.getTeam2().getName(),
                                        match.getTeam1Goals(), match.getTeam2Goals(), match.getMatchDate(),
                                        match.getEvents()
                                                        .stream()
                                                        .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
                                                        .toList());
                } else {
                        return null;
                }
        }

        public List<MatchEvent> getMatchWithPlayerEvents(Integer matchId, Integer playerId) {
                List<MatchEventEntity> matchEvents = matchEventRepository.findByMatchIdAndPlayer(matchId, playerId);

                return matchEvents.stream()
                                .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
                                .toList();
        }

        public List<MatchEvent> getMatchEventsOfType(Integer matchId, Integer eventType) {
                return matchEventRepository.findByIdIncludeEventsOfType(matchId, eventType).stream()
                                .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
                                .toList();
        }

        public Integer getTotalPlayersWithMoreThanNMatches(int num_matches) {
                return playerRepository.getTotalPlayersWithMoreThanNMatches(num_matches);
        }

        public List<MatchEvent> getMatchWithPlayerEventsError(Integer matchId, Integer playerId) {
                List<MatchEventEntity> matchEvents = matchEventRepository.findByMatchIdAndPlayerError(matchId, playerId);

                return matchEvents.stream()
                                .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
                                .toList();
        } 

}
