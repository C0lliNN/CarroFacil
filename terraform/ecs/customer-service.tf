resource "aws_ecs_task_definition" "customer_service_task" {
  family             = "customer_service"
  network_mode       = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                = 2048
  memory             = 4096
  execution_role_arn = aws_iam_role.task_definition_execution_role.arn
  task_role_arn      = aws_iam_role.task_definition_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "customer_service"
      image     = "c0lllinn/cf-customer-service:latest"
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
          value = var.customer_service_db_table_name
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
          name  = "SPRING_DOCKER_COMPOSE_ENABLED"
          value = "false"
        },
        {
          name = "AWS_QUEUES_BOOKING"
          value = var.aws_queues_bookings
        }
      ]

      logConfiguration = {
        "logDriver" = "awslogs",
        "options" = {
          "awslogs-group"         = var.customer_service_log_group_name,
          "awslogs-region"        = var.region,
          "awslogs-stream-prefix" = "customer-service"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "customer_service" {
  name            = "customer_service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.customer_service_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.subnet_ids
    security_groups = [var.security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = module.customer_service_alb.tg_alb_arn
    container_name   = aws_ecs_task_definition.customer_service_task.family
    container_port   = 80
  }
}

module "customer_service_alb" {
  source = "../alb"

  name              = "customer-service"
  vpc_id            = var.vpc_id
  subnets           = var.subnet_ids
  security_group_id = var.security_group_id
}
