resource "aws_ecs_task_definition" "inventory_management_task" {
  family                   = "inventory_management"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048
  execution_role_arn       = aws_iam_role.task_definition_execution_role.arn
  task_role_arn            = aws_iam_role.task_definition_task_role.arn

  container_definitions = jsonencode([
    {
      name         = "inventory_management"
      image        = "c0lllinn/cf-inventory-management:latest"
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
          value = "jdbc:postgresql://${var.inventory_management_db_endpoint}/${var.inventory_management_db_name}?useSSL=false&serverTimezone=UTC"
        },
        {
          name  = "SPRING_DATASOURCE_USERNAME"
          value = var.inventory_management_db_username
        },
        {
          name  = "SPRING_DATASOURCE_PASSWORD"
          value = var.inventory_management_db_password
        },
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name = "AUTH_AUTHORIZATIONSERVER_URL"
          value = "http://${module.user_management_alb.alb_dns_name}/auth/validate"
        }
      ]

      logConfiguration = {
        "logDriver" = "awslogs",
        "options" = {
          "awslogs-group"         = var.inventory_management_log_group_name,
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "inventory-management"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "inventory_management_service" {
  name            = "inventory_management_service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.inventory_management_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  network_configuration {
    subnets          = var.subnet_ids
    security_groups  = [var.security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = module.inventory_management_alb.tg_alb_arn
    container_name = aws_ecs_task_definition.inventory_management_task.family
    container_port = 80
  }
}

module "inventory_management_alb" {
  source = "../alb"

  name              = "inventory-management"
  vpc_id            = var.vpc_id
  subnets           = var.subnet_ids
  security_group_id = var.security_group_id
}
