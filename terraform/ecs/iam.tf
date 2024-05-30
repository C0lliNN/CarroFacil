resource "aws_iam_role" "task_definition_execution_role" {
  name               = "cf-execution-role"
  assume_role_policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
          "Service" : "ecs-tasks.amazonaws.com"
        },
        "Effect" : "Allow",
        "Sid" : ""
      }
    ]
  })
}

resource "aws_iam_policy" "execution_role_policy" {
  name        = "cf-execution-role-policy"
  description = "Allow Cloudwatch and Logs access"
  policy      = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Sid" : "Stmt1674841846576",
        "Action" : "cloudwatch:*",
        "Effect" : "Allow",
        "Resource" : "*"
      },
      {
        "Sid" : "Stmt1674841846578",
        "Action" : "logs:*",
        "Effect" : "Allow",
        "Resource" : "*"
      }
    ]
  })
}

resource "aws_iam_policy_attachment" "execution_role_policy_attachment" {
  name       = "$cf-execution-role-policy-attachment"
  roles      = [aws_iam_role.task_definition_execution_role.name]
  policy_arn = aws_iam_policy.execution_role_policy.arn
}


resource "aws_iam_role" "task_definition_task_role" {
  name               = "cf-role"
  assume_role_policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
          "Service" : "ecs-tasks.amazonaws.com"
        },
        "Effect" : "Allow",
        "Sid" : ""
      }
    ]
  })
}

resource "aws_iam_policy" "task_role_policy" {
  name        = "cf-policy"
  description = "Allow full S3 Access"
  policy      = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Sid" : "Stmt1674840832992",
        "Action" : "s3:*",
        "Effect" : "Allow",
        "Resource" : "*"
      }, {
        "Sid" : "Stmt1674841846579",
        "Action" : "sqs:*",
        "Effect" : "Allow",
        "Resource" : "*"
      },
      {
        "Sid" : "Stmt1675841846579",
        "Action" : "sns:*",
        "Effect" : "Allow",
        "Resource" : "*"
      },
      {
        "Sid" : "Stmt1674841846572",
        "Action" : "dynamodb:*",
        "Effect" : "Allow",
        "Resource" : "*"
      },
      {
        "Sid" : "Stmt1674841846571",
        "Action" : "rds:*",
        "Effect" : "Allow",
        "Resource" : "*"
      }
    ]
  })
}

resource "aws_iam_policy_attachment" "task_role_policy_attachment" {
  name       = "cf-policy-attachment"
  roles      = [aws_iam_role.task_definition_task_role.name]
  policy_arn = aws_iam_policy.task_role_policy.arn
}