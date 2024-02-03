echo 'Get events of match 400222852'
curl http://localhost:8080/football/matches/400222852/timeline
echo 'Get events of type 20 in match 400222852'
curl http://localhost:8080/football/matches/400222852/timeline/events/20
echo 'Get events of player 358189 in match 400222852'
curl http://localhost:8080/football/matches/400222852/timeline/358189
echo 'Get number of players with more than 2 matches'
curl http://localhost:8080/football/players/matches/2

echo 'Next request will fail'
curl http://localhost:8080/football/matches/400222852/timeline/358189/error
