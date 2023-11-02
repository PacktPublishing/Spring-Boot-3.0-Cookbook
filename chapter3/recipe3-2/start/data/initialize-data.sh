# mongoimport --uri=mongodb://packt:packt@0.0.0.0:27017/?authSource=admin --db=football --collection=teams --jsonArray < teams.json
# mongoimport --uri=mongodb://packt:packt@0.0.0.0:27017/?authSource=admin --db=football --collection=players --jsonArray < players.json
# mongoimport --uri=mongodb://packt:packt@0.0.0.0:27017/?authSource=admin --db=football --collection=matches --jsonArray < matches.json
# mongoimport --uri=mongodb://packt:packt@0.0.0.0:27017/?authSource=admin --db=football --collection=match_events --jsonArray < events.json

mongoimport --uri="mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000" --db=football --collection=teams --jsonArray < teams.json
mongoimport --uri="mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000" --db=football --collection=players --jsonArray < players.json
mongoimport --uri="mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000" --db=football --collection=matches --jsonArray < matches.json
mongoimport --uri="mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000" --db=football --collection=match_events --jsonArray < events.json
