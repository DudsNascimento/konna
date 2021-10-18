terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 3.20.0"
    }

    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.0.1"
    }
  }
}

data "terraform_remote_state" "eks" {
  backend = "local"

  config = {
    path = "../terraform-mutant-finder-eks-cluster/terraform.tfstate"
  }
}

# Retrieve EKS cluster information
provider "aws" {
  region = data.terraform_remote_state.eks.outputs.region
}

data "aws_eks_cluster" "cluster" {
  name = data.terraform_remote_state.eks.outputs.cluster_id
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  exec {
    api_version = "client.authentication.k8s.io/v1alpha1"
    command     = "aws"
    args = [
      "eks",
      "get-token",
      "--cluster-name",
      data.aws_eks_cluster.cluster.name
    ]
  }
}

resource "kubernetes_namespace" "prod_namespace" {
  metadata {
    name = "prod-namespace"
  }
}

resource "kubernetes_deployment" "konna" {
  metadata {
    name = "scalable-mutant-finder"
    namespace = "${kubernetes_namespace.prod_namespace.metadata.name}"
    labels = {
      App = "ScalableMutantFinder"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        App = "ScalableMutantFinder"
      }
    }
    template {
      metadata {
        labels = {
          App = "ScalableMutantFinder"
        }
      }
      spec {
        container {
          image = "473200936731.dkr.ecr.us-east-2.amazonaws.com/konna:v0.0.1"
          image_pull_policy = "Always"
          name  = "mutant-finder"

          port {
            container_port = 5000
          }

          resources {
            limits = {
              cpu    = "0.5"
              memory = "512Mi"
            }
            requests = {
              cpu    = "100m"
              memory = "50Mi"
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "konna" {
  metadata {
    name = "mutant-finder"
    namespace = "prod-namespace"
  }
  spec {
    selector = {
      App = kubernetes_deployment.konna.spec.0.template.0.metadata[0].labels.App
    }
    port {
      port        = 80
      target_port = 5000
    }

    type = "LoadBalancer"
  }
}

output "lb_ip" {
  value = kubernetes_service.konna.status.0.load_balancer.0.ingress.0.hostname
}
