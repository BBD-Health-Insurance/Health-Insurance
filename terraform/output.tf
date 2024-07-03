output "web_domain_url" {
  value = aws_elastic_beanstalk_environment.web_env.name
}

output "db_instance_address" {
  value = aws_db_instance.db.address
}

output "eb_web_env_name" {
  value = aws_elastic_beanstalk_environment.web_env.name
}

output "eb_web_app_name" {
  value = aws_elastic_beanstalk_application.web_app.name
}

output "github_action_role_arn" {
  value = aws_iam_role.github_action_role.arn
}

output "aws_region" {
  value = var.region
}

output "frontend_bucket_name" {
  value = aws_s3_bucket.bucket.bucket
}

output "api_bucket_name" {
  value = aws_s3_bucket.api_bucket.bucket
}

