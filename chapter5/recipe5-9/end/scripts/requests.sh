echo 'Create some users'
for i in {1..10}; do
    curl --header "Content-Type: application/text" --request POST --data "User$i" http://localhost:8080/users
done

echo 'Get all users'
curl http://localhost:8080/users

echo 'Buy albums for users'
for i in {1..10}; do
    curl --header "Content-Type: application/text" --request POST --data "Album$i" http://localhost:8080/albums/$i
done

echo 'Buy cards for users'
for i in {1..10}; do
    curl -X 'POST' http://localhost:8080/albums/$i/cards \
        -H 'accept: */*' \
        -H 'Content-Type: application/json' \
        -d '15'
done

echo 'Auto assign cards'
for i in {1..10}; do
    curl --header "Content-Type: application/text" --request POST http://localhost:8080/albums/$i/auto
done

echo 'Trade between users'
for i in {1..9}; do
    next=$((i+1))    
    for ((j=$next; j<11; j++)); do
        echo "Trade between users $i and $j"
        curl --header "Content-Type: application/text" --request POST http://localhost:8080/albums/trade/$i/$j
    done
done

echo 'Get users trading state'
for i in {1..10}; do
curl -X 'GET' \
  http://localhost:8080/albums/users/$i \
  -H 'accept: */*'
done

echo 'Transfer card 4082 to user 2
curl -X 'POST' \
  'http://localhost:8080/albums/2/transfer/4082' \
  -H 'accept: */*' \
  -d ''