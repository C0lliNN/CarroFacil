output "booking_service_url" {
  value = module.booking_service_alb.alb_dns_name
}

output "inventory_management_url" {
  value = module.inventory_management_alb.alb_dns_name
}

output "user_management_url" {
  value = module.user_management_alb.alb_dns_name
}

output "customer_service_url" {
  value = module.customer_service_alb.alb_dns_name
}

output "employee_service_url" {
  value = module.employee_service_alb.alb_dns_name
}
