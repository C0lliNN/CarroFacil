# RDS for Inventory Management
resource "aws_db_instance" "inventory_management_db" {
  identifier             = "inventory-management-db"
  allocated_storage      = 20
  storage_type           = "gp2"
  engine                 = "postgres"
  engine_version         = "16.2"
  instance_class         = "db.t3.micro"
  username               = "inventory_management_application"
  password               = var.inventory_management_password
  parameter_group_name   = "default.postgres16"
  db_name                = "inventory_management"
  skip_final_snapshot    = true
  publicly_accessible    = true
  vpc_security_group_ids = [var.security_group_id]
  db_subnet_group_name   = aws_db_subnet_group.db_subnet_group.name
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "db_subnet_group"
  subnet_ids = var.subnet_ids
}

# RDS for BookingService
resource "aws_db_instance" "booking_service_db" {
  identifier             = "booking-service-db"
  allocated_storage      = 20
  storage_type           = "gp2"
  engine                 = "postgres"
  engine_version         = "16.2"
  instance_class         = "db.t3.micro"
  username               = "booking_application"
  password               = var.booking_service_password
  db_name                = "booking_service"
  parameter_group_name   = "default.postgres16"
  skip_final_snapshot    = true
  publicly_accessible    = true
  vpc_security_group_ids = [var.security_group_id]
  db_subnet_group_name   = aws_db_subnet_group.db_subnet_group.name
}