 curl -v -u jlong:pw  http://localhost:8080/graphql \
     -H 'Content-Type: application/json' \
     --data-raw '{"query":"{ customerById(id:1) { id, name } }" }'

 # curl -v -u rwinch:pw  http://localhost:8080/graphql \
 #     -H 'Content-Type: application/json' \
 #     --data-raw '{"query":"mutation { insert(name: \"Josh\") { id, name } }" }'