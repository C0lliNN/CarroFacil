output "users_table" {
  value = aws_dynamodb_table.usermanagement_table.name
}

output "customers_table" {
  value = aws_dynamodb_table.customers_table.name
}

output "employees_table" {
  value = aws_dynamodb_table.employees_table.name
}

output "inventory_management_db_endpoint" {
  value = aws_db_instance.inventory_management_db.endpoint
}

output "inventory_management_db_name" {
  value = aws_db_instance.inventory_management_db.db_name
}

output "inventory_management_db_username" {
  value = aws_db_instance.inventory_management_db.username
}

output "inventory_management_db_password" {
  value = aws_db_instance.inventory_management_db.password
}

output "booking_service_db_endpoint" {
  value = aws_db_instance.booking_service_db.endpoint
}

output "booking_service_db_name" {
  value = aws_db_instance.booking_service_db.db_name
}

output "booking_service_db_username" {
  value = aws_db_instance.booking_service_db.username
}

output "booking_service_db_password" {
  value = aws_db_instance.booking_service_db.password
}
