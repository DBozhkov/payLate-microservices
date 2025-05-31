#!/bin/bash

DOCKER_USER="dbozhkov"

SERVICES=(admin-service cart-service messaging-service order-service payment-service product-service review-service)

for SERVICE in "${SERVICES[@]}"
do
  echo "Building $SERVICE ..."
  docker build -t $DOCKER_USER/$SERVICE:latest ./$SERVICE
  echo "Pushing $SERVICE ..."
  docker push $DOCKER_USER/$SERVICE:latest
done

echo "All services built and pushed successfully."