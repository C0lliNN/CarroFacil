# SQS Queue for Bookings
resource "aws_sqs_queue" "bookings_queue" {
  name = "cf-bookings"
}

# SNS Topic for Bookings
resource "aws_sns_topic" "bookings_topic" {
  name = "cf-bookings"
}

resource "aws_sns_topic_subscription" "bookings_subscription" {
  topic_arn = aws_sns_topic.bookings_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.bookings_queue.arn
}

output "bookings_topic" {
  value = aws_sns_topic.bookings_topic.arn
}

output "bookings_queue" {
  value = aws_sqs_queue.bookings_queue.arn
}

output "bookings_queue_name" {
  value = aws_sqs_queue.bookings_queue.name
}
