resource "aws_kms_key" "konna_cluster_aws_kms_key" {
  description             = "Cluster KMS key"
  deletion_window_in_days = 7
}

resource "aws_cloudwatch_log_group" "konna_log_group" {
  name = "konna-log-group"
}

resource "aws_ecs_cluster" "konna_ecs_cluster" {
  name = "konna-ecs-cluster"

  configuration {
    execute_command_configuration {
      kms_key_id = aws_kms_key.konna_cluster_aws_kms_key.arn
      logging    = "OVERRIDE"

      log_configuration {
        cloud_watch_encryption_enabled = true
        cloud_watch_log_group_name     = aws_cloudwatch_log_group.konna_log_group.name
      }
    }
  }
}