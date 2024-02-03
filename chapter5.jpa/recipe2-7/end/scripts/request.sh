echo 'Searching players from team 1882879 with different criteria'
curl -X 'GET' \
  'http://localhost:8080/queries/team/1882879/players?name=ara&minHeight=170&maxHeight=180&minWeight=69&maxWeight=71' \
  -H 'accept: */*'

echo 'Searching players from team 1882879 whose name contains BE'
curl -X 'GET' \
  'http://localhost:8080/queries/team/1882879/players?name=BE' \
  -H 'accept: */*'


echo 'Searching events from match 400258554 between minutes 10:30 and 10:40'
curl -X 'GET' \
  'http://localhost:8080/queries/match/400258554/events?minMinute=2023-08-20T10%3A30%3A00&maxMinute=2023-08-20T10%3A40%3A00' \
  -H 'accept: */*'

echo 'Get missing players for user 1'
curl -X 'GET' \
  'http://localhost:8080/queries/user/1/missing' \
  -H 'accept: */*'

