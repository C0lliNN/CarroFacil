variable "inventory_management_db_password" {
  description = "The endpoint of the inventory management database"
}

variable "booking_service_db_password" {
  description = "The endpoint of the booking service database"
}

variable "jwt_secret" {
    description = "The secret for the JWT token"
}

variable "jwt_duration" {
    description = "The duration of the JWT token"
}

variable "employee_email" {
    description = "The email of the employee"
}

variable "employee_password" {
    description = "The password of the employee"
}