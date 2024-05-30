variable "inventory_management_password" {
    description = "Password for the inventory management database"
}

variable "booking_service_password" {
  description = "Password for the booking service database"
}

variable "security_group_id" {
  description = "The ID of the security group to use for the Database"
}

variable "subnet_ids" {
  description = "The IDs of the subnets to use for the Database"
}