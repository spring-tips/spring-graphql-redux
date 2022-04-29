java -jar rsc-0.9.1.jar    \
  --request   \
  --route=graphql --dataMimeType="application/graphql+json" \
  --data='{"query":"{  greeting { greeting } }" }' \
  --debug tcp://localhost:9191


