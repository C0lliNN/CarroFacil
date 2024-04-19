aws --endpoint http://localhost:4566 dynamodb create-table \
  --table-name users \
  --attribute-definitions AttributeName=id,AttributeType=S AttributeName=email,AttributeType=S\
  --key-schema AttributeName=id,KeyType=HASH AttributeName=email,KeyType=RANGE \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
