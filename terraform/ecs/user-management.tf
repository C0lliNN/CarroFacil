resource "aws_ecs_task_definition" "user_management_task" {
  family             = "user_management"
  network_mode       = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                = 2048
  memory             = 4096
  execution_role_arn = aws_iam_role.task_definition_execution_role.arn
  task_role_arn      = aws_iam_role.task_definition_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "user_management"
      image     = "c0lllinn/cf-user-management:latest"
      essential = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
        }
      ],
      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name  = "SERVER_PORT"
          value = "80"
        },
        {
          name  = "DYNAMODB_TABLE_NAME"
          value = var.user_management_db_table_name
        },
        {
          name  = "AWS_REGION"
          value = "us-east-1"
        },
        {
          name  = "SPRING_CLOUD_AWS_DYNAMODB_REGION",
          value = "us-east-1"
        },
        {
          name  = "AWS_QUEUES_BOOKINGS"
          value = var.aws_queues_bookings
        },
        {
          name  = "SPRING_DOCKER_COMPOSE_ENABLED"
          value = "false"
        },
        {
          name  = "JWT_SECRET"
          value = var.jwt_secret
        },
        {
          name  = "EMPLOYEE_EMAIL"
          value = var.employee_email
        },
        {
          name  = "EMPLOYEE_PASSWORD"
          value = var.employee_password
        }
      ]

      logConfiguration = {
        "logDriver" = "awslogs",
        "options" = {
          "awslogs-group"         = var.user_management_log_group_name,
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "user-management"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "user_management_service" {
  name            = "user_management_service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.user_management_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.subnet_ids
    security_groups = [var.security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = module.user_management_alb.tg_alb_arn
    container_name   = aws_ecs_task_definition.user_management_task.family
    container_port   = 80
  }
}

module "user_management_alb" {
  source = "../alb"

  name              = "user-management"
  vpc_id            = var.vpc_id
  subnets           = var.subnet_ids
  security_group_id = var.security_group_id
}
