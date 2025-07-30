#!/bin/bash


# Backend services ()
backend_services=(
  "admin-service"
  "cart-service"
  "messaging-service"
  "order-service"
  "payment-service"
  "product-service"
  "review-service"
)

echo "Building backend service JARs and Docker images..."
for service in "${backend_services[@]}"; do
  echo "---------------------------------"
  echo "Building JAR for $service..."
  (cd "backend/$service" && mvn clean package -DskipTests)

  echo "Building $service:latest Docker image..."
  docker build -t "$service:latest" -f "./backend/$service/Dockerfile" .

  echo "Loading $service:latest into Minikube..."
  minikube image load "$service:latest"
  echo "$service:latest processed."
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