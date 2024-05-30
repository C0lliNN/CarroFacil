terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}
provider "aws" {
  region = "us-east-1"
}

module "cloudwatch" {
  source = "./cloudwatch"
}

module "network" {
  source = "./network"
}

module "messaging" {
  source = "./messaging"
}

module "database" {
  source = "./database"

  subnet_ids                    = module.network.subnet_ids
  booking_service_password      = var.booking_service_db_password
  inventory_management_password = var.inventory_management_db_password
  security_group_id             = module.network.security_group_id
}

module "ecs" {
  source = "./ecs"

  subnet_ids                          = module.network.subnet_ids
  security_group_id                   = module.network.security_group_id
  inventory_management_db_endpoint    = module.database.inventory_management_db_endpoint
  inventory_management_db_name        = module.database.inventory_management_db_name
  inventory_management_db_username    = module.database.inventory_management_db_username
  inventory_management_db_password    = module.database.inventory_management_db_password
  booking_service_db_endpoint         = module.database.booking_service_db_endpoint
  booking_service_db_name             = module.database.booking_service_db_name
  booking_service_db_username         = module.database.booking_service_db_username
  booking_service_db_password         = module.database.booking_service_db_password
  jwt_secret                          = var.jwt_secret
  user_management_db_table_name       = module.database.users_table
  aws_queues_bookings                 = module.messaging.bookings_queue_name
  region                              = "us-east-1"
  user_management_log_group_name      = module.cloudwatch.user_management_log_group_name
  inventory_management_log_group_name = module.cloudwatch.inventory_management_log_group_name
  booking_service_log_group_name      = module.cloudwatch.booking_log_group_name
  vpc_id                              = module.network.vpc_id
}







