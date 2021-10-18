#!/bin/bash
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 473200936731.dkr.ecr.us-east-2.amazonaws.com
docker tag konna:latest 473200936731.dkr.ecr.us-east-2.amazonaws.com/konna:v0.0.1
docker push 473200936731.dkr.ecr.us-east-2.amazonaws.com/konna:v0.0.1