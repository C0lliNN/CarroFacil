resource "aws_ecs_task_definition" "booking_service_task" {
  family                   = "booking_service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 2048
  memory                   = 4096

  task_role_arn      = aws_iam_role.task_definition_task_role.arn
  execution_role_arn = aws_iam_role.task_definition_execution_role.arn

  container_definitions = jsonencode([
    {
      name         = "booking_service"
      image        = "c0lllinn/cf-booking-service:latest"
      essential    = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
        }
      ],
      environment = [
        {
          name  = "SERVER_PORT"
          value = "80"
        },
        {
          name  = "SPRING_DATASOURCE_URL"
          value = "jdbc:postgresql://${var.booking_service_db_endpoint}/${var.booking_service_db_name}?useSSL=false&serverTimezone=UTC"
        },
        {
          name  = "SPRING_DATASOURCE_USERNAME"
          value = var.booking_service_db_username
        },
        {
          name  = "SPRING_DATASOURCE_PASSWORD"
          value = var.booking_service_db_password
        },
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
            name = "INVENTORYMANAGEMENT_CLIENT_BASEURL"
            value = "http://${module.inventory_management_alb.alb_dns_name}"
        },
        {
          name = "AUTH_AUTHORIZATIONSERVER_URL"
          value = "http://${module.user_management_alb.alb_dns_name}/validate"
        },
        {
            name = "SNS_TOPIC_BOOKINGS"
            value = var.aws_sns_topic_bookings
        }
      ]

      logConfiguration = {
        "logDriver" = "awslogs",
        "options" = {
          "awslogs-group"         = var.booking_service_log_group_name,
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "booking-service"
        }
      }
    }
  ])


}

resource "aws_ecs_service" "booking_service_service" {
  name            = "booking_service_service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.booking_service_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  network_configuration {
    subnets          = var.subnet_ids
    security_groups  = [var.security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = module.booking_service_alb.tg_alb_arn
    container_name = aws_ecs_task_definition.booking_service_task.family
    container_port = 80
  }
}

module "booking_service_alb" {
  source = "../alb"

  name              = "booking-service"
  vpc_id            = var.vpc_id
  subnets           = var.subnet_ids
  security_group_id = var.security_group_id
}
