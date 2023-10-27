echo 'Get a team'
curl http://localhost:8080/football/team/1884823

echo 'Get a team by name'
curl http://localhost:8080/football/team?name=New%20Zealand

echo 'Get a team by name using SQL syntax'
curl http://localhost:8080/football/team/New%20Zealand/sql

echo 'Get the teams that contain "erm"'
curl http://localhost:8080/football/team/erm/contains

echo 'Get a player'
curl http://localhost:8080/football/player/387138

echo 'Upsert a team'
curl --request POST -H "Content-Type: application/json" -d '{"id":"99999999", "name":"Mars"}' http://localhost:8080/football/team
echo 'Upsert a team'
curl --request POST -H "Content-Type: application/json" -d '{"id":"99999999", "name":"Mars", "players":[{"id": "88888888","jerseyNumber": 2,"name": "E.T.","position": "Defender", "dateOfBirth": "1980-06-10", "height": 89, "weight": 54}]}' http://localhost:8080/football/team

echo 'Update team name'
curl --request PATCH http://localhost:8080/football/team/99999999?name=Mars%20the%20planet

echo 'Delete a team'
curl --request DELETE http://localhost:8080/football/team/99999999
