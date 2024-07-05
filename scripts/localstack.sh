awslocal dynamodb create-table --table-name users \
  --attribute-definitions AttributeName=id,AttributeType=S\
  --key-schema AttributeName=id,KeyType=HASH \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

# Create secondary index for email
awslocal dynamodb update-table --table-name users \
  --attribute-definitions AttributeName=email,AttributeType=S \
  --global-secondary-index-updates '[{"Create": {"IndexName": "emailIndex", "KeySchema": [{"AttributeName": "email", "KeyType": "HASH"}], "Projection": {"ProjectionType": "ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 5, "WriteCapacityUnits": 5}}}]'

# Create a user
awslocal dynamodb put-item --table-name users --item \
'{"id": {"S": "36688128-87b0-4195-ad6e-8a74c4bebd38"}, "name": {"S": "Raphael Collin"}, "email": {"S": "test@test.com"}, "password": {"S": "$2a$10$4VRhYGSlgyMKMVAuYwv9Du7qBXbwKJulg/S/zIL8nL9MzJxtWcaC"}}'

# Get Item by Email
awslocal dynamodb query --table-name users --index-name emailIndex --key-condition-expression "email = :email" \
  --expression-attribute-values '{":email": {"S": "test@test.com"}}'

awslocal sns create-topic --name booking-events

awslocal sqs create-queue --queue-name bookings

awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:booking-events  --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:bookings
