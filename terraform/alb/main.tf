variable "name" {}
variable "vpc_id" {}
variable "subnets" {}
variable "security_group_id" {}

resource "aws_alb" "alb" {
  name               = "${var.name}-alb"
  internal           = false
  load_balancer_type = "application"
  subnets            = var.subnets
  security_groups    = [var.security_group_id]
}

resource "aws_alb_target_group" "target_group" {
  name     = "${var.name}-tg"
  port     = 80
  protocol = "HTTP"
  health_check {
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
  target_type = "ip"
  vpc_id   = var.vpc_id
}

resource "aws_alb_listener" "http_listener" {
  load_balancer_arn = aws_alb.alb.id
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = aws_alb_target_group.target_group.id
    type             = "forward"
  }
}

output "alb_dns_name" {
  value = aws_alb.alb.dns_name
}

output "tg_alb_arn" {
  value = aws_alb_target_group.target_group.arn
}