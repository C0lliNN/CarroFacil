resource "aws_cloudwatch_log_group" "user_management_log_group" {
  name = "apps/user-management"
}

resource "aws_cloudwatch_log_group" "inventory_management_log_group" {
  name = "apps/inventory-management"
}

resource "aws_cloudwatch_log_group" "booking_service_log_group" {
  name = "apps/booking-service"
}

resource "aws_cloudwatch_log_group" "customer_service_log_group" {
  name = "apps/customer-service"
}

resource "aws_cloudwatch_log_group" "employee_service_log_group" {
  name = "apps/employee-service"
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

output "customer_log_group_name" {
  value = aws_cloudwatch_log_group.customer_service_log_group.name
}

output "employee_log_group_name" {
  value = aws_cloudwatch_log_group.employee_service_log_group.name
}