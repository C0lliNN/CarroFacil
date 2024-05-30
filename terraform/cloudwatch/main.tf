resource "aws_cloudwatch_log_group" "user_management_log_group" {
  name = "apps/user-management"
}

resource "aws_cloudwatch_log_group" "inventory_management_log_group" {
  name = "apps/inventory-management"
}

resource "aws_cloudwatch_log_group" "booking_service_log_group" {
  name = "apps/booking-service"
}

output "user_management_log_group_name" {
  value = aws_cloudwatch_log_group.user_management_log_group.name
}

output "inventory_management_log_group_name" {
  value = aws_cloudwatch_log_group.inventory_management_log_group.name
}

output "booking_log_group_name" {
  value = aws_cloudwatch_log_group.booking_service_log_group.name
}