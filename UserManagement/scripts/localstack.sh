awslocal dynamodb create-table --table-name users \
  --attribute-definitions AttributeName=id,AttributeType=S AttributeName=email,AttributeType=S\
  --key-schema AttributeName=id,KeyType=HASH AttributeName=email,KeyType=RANGE \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

awslocal sqs create-queue --queue-name bookings