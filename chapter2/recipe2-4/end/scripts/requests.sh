echo 'Retrieving team 1884823'
curl http://localhost:8080/football/teams/1884823

echo 'Get players of match 400222832'
curl http://localhost:8080/football/matches/400222832/players

echo 'Get the players of team 1884881 in album 1'
curl http://localhost:8080/football/albums/1/1884881/players

echo 'Searh players starting by Li'
curl http://localhost:8080/football/players/startwith?startingName=Li

echo 'Searh players like smi'
curl http://localhost:8080/football/players/search?q=smi

echo 'Get a list of players'
curl http://localhost:8080/football/players/list?players=215194,251016

echo 'Get paged results of the players I have'
curl http://localhost:8080/football/albums/1/myplayers

echo 'Get the number of Midfielders per team'
url http://localhost:8080/football/teams/Midfielder/count

echo 'Get the number of Goalkeepers per team'
url http://localhost:8080/football/teams/Goalkeeper/count

echo 'Get all players paginated. First page, 10 elements'
curl http://localhost:8080/football/players/paginated?page=1&size=10