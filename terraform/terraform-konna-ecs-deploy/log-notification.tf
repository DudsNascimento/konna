resource "aws_cloudwatch_log_metric_filter" "konna_log_metric_filter" {
  name           = "konna-log-metric-filter"
  pattern        = "{ $.logger = \"AthletesServiceImpl\" }"
  log_group_name = var.konnaLogGroup

  metric_transformation {
    name      = "athlete-listing-count"
    namespace = "default"
    value     = "1"
  }

  depends_on = [aws_ecs_task_definition.konna_deploy_ecs_task_definition]
}

resource "aws_sns_topic" "konna_log_metric_alarm_sns" {
  name  = "konna-log-metric-alarm-sns"
}

resource "aws_sns_topic_subscription" "konna_log_metric_alarm_sns_subscription" {
  topic_arn = aws_sns_topic.konna_log_metric_alarm_sns.arn
  protocol  = "email"
  endpoint  = "tiagomarques.ufjf@gmail.com"
}

resource "aws_cloudwatch_metric_alarm" "konna_log_metric_alarm" {
  alarm_name                = "konna-log-metric-alarm"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "2"
  metric_name               = "athlete-listing-count"
  namespace                 = "default"
  period                    = "60"
  statistic                 = "SampleCount"
  threshold                 = "1"
  alarm_description         = "This counts the number of athletes listings"
  alarm_actions             = [aws_sns_topic.konna_log_metric_alarm_sns.arn]
  ok_actions                = []
  insufficient_data_actions = []

  depends_on = [aws_cloudwatch_log_metric_filter.konna_log_metric_filter]
}