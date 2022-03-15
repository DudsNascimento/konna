output "konna_log_group_name" {
  value = aws_cloudwatch_log_group.konna_log_group.name
}

output "konna_ecs_cluster_name" {
  value = aws_ecs_cluster.konna_ecs_cluster.name
}

output "konna_ecs_task_definition_family" {
  value = aws_ecs_task_definition.konna_deploy_ecs_task_definition.family
}

output "konna_ecs_service_name" {
  value = aws_ecs_service.konna_ecs_service.name
}

output "konna_load_balancer_dns_name" {
  value = aws_alb.konna_application_load_balancer.dns_name
}
