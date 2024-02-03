echo 'Retrieving team 1884823'
curl http://localhost:8080/football/teams/1884823

echo 'Retrieving players which name contains "HER"'
curl http://localhost:8080/football/players?search=HER

echo 'Retrieving players born on 2000-06-12'
curl http://localhost:8080/football/players/birth/2000-06-12

echo 'Creating a Antarctica team'
curl --request POST -H "Content-Type: application/text" -d 'Antarctica' http://localhost:8080/football/teams

echo 'Updating player 377762 position to Forward'
curl --header "Content-Type: application/text"  --request PUT --data 'Forward' http://localhost:8080/football/player/377762/position

echo 'Updating player 377762 position to Midfielder'
curl --header "Content-Type: application/text"  --request PUT --data 'Midfielder' http://localhost:8080/football/player/377762/position