output "booking_service_url" {
  value = module.ecs.booking_service_url
}

output "inventory_management_url" {
  value = module.ecs.inventory_management_url
}

output "user_management_url" {
  value = module.ecs.user_management_url
}

output "customer_service_url" {
  value = module.ecs.customer_service_url
}

output "employee_service_url" {
  value = module.ecs.employee_service_url
}

output "inventory_management_db_endpoint" {
  value = module.database.inventory_management_db_endpoint
}

output "booking_service_db_endpoint" {
  value = module.database.booking_service_db_endpoint
}