#!/bin/bash

# Ensure minikube is running and the 'minikube' command is in your PATH.

# Backend services
backend_services=(
  "admin-service"
  "cart-service"
  "messaging-service"
  "order-service"
  "payment-service"
  "product-service"
  "review-service"
)

echo "Building and loading backend service images..."
for service in "${backend_services[@]}"; do
  echo "Building $service:latest..."
docker build -t "$service:latest" -f "./$service/Dockerfile" .
  echo "Loading $service:latest into Minikube..."
  minikube image load "$service:latest"
  echo "$service:latest processed."
  echo "---------------------------------"
done

echo "All backend service images processed."
echo ""
echo "Building and loading frontend service image..."
echo "Building frontend-app:latest..."
docker build -t "frontend-app:latest" "./frontend/react-paylateapp"
echo "Loading frontend-app:latest into Minikube..."
minikube image load "frontend-app:latest"
echo "frontend-app:latest processed."
echo "---------------------------------"
echo "All images built and loaded into Minikube."
