resource "aws_default_vpc" "default_vpc" {
}

resource "aws_default_subnet" "default_subnet_a" {
  availability_zone = "sa-east-1a"
}

resource "aws_default_subnet" "default_subnet_b" {
  availability_zone = "sa-east-1b"
}

resource "aws_default_subnet" "default_subnet_c" {
  availability_zone = "sa-east-1c"
}

resource "aws_security_group" "konna_ecs_service_security_group" {
  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    security_groups = ["${aws_security_group.load_balancer_security_group.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_ecs_service" "konna_ecs_service" {
  name            = "konna-ecs-service"
  cluster         = "${aws_ecs_cluster.konna_ecs_cluster.id}"
  task_definition = "${aws_ecs_task_definition.konna_deploy_ecs_task_definition.arn}"
  launch_type     = "FARGATE"
  desired_count   = 1

  load_balancer {
    target_group_arn = "${aws_lb_target_group.target_group.arn}"
    container_name   = "${var.konnaContainerName}"
    container_port   = 8080
  }

  network_configuration {
    subnets          = [
        "${aws_default_subnet.default_subnet_a.id}",
        "${aws_default_subnet.default_subnet_b.id}",
        "${aws_default_subnet.default_subnet_c.id}"
    ]
    assign_public_ip = true
    security_groups  = ["${aws_security_group.konna_ecs_service_security_group.id}"]
  }
}