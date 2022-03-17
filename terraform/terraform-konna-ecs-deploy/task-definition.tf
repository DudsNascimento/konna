resource "aws_ecs_task_definition" "konna_deploy_ecs_task_definition" {
  family                   = "konna-task"
  container_definitions    = jsonencode([
    {
      "name": "${var.konnaContainerName}",
      "image": "${var.konnaDockerImageUrl}",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "hostPort": 8080
        }
      ],
      "memory": 512,
      "cpu": 256,
      "logConfiguration": {
          "logDriver": "awslogs",
          "options": {
              "awslogs-group": "${var.konnaLogGroup}",
              "awslogs-region": "sa-east-1",
              "awslogs-stream-prefix": "awslogs-konna"
          }
      },
    }
  ])
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  memory                   = 512
  cpu                      = 256
  execution_role_arn       = "${aws_iam_role.ecs_task_execution_role.arn}"
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name               = "ecs_task_execution_role"
  assume_role_policy = "${data.aws_iam_policy_document.assume_role_policy.json}"
}

data "aws_iam_policy_document" "assume_role_policy" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = "${aws_iam_role.ecs_task_execution_role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}
