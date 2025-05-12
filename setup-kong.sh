#!/bin/bash
set -e

# Wait for Kong to be ready
echo "Waiting for Kong Admin API to be ready..."
until curl -s http://kong:8001/status > /dev/null; do
  echo "Kong Admin API is not ready yet, waiting..."
  sleep 5
done

echo "Kong Admin API is ready, configuring services and routes..."

# Load the configuration from the JSON file
CONFIG=$(cat /kong-config/kong-config.json)

# Extract services from the JSON
SERVICES=$(echo $CONFIG | jq -c '.services[]')

# For each service in the configuration
for SERVICE in $SERVICES; do
  # Extract service details
  SERVICE_NAME=$(echo $SERVICE | jq -r '.name')
  SERVICE_URL=$(echo $SERVICE | jq -r '.url')
  
  echo "Creating service: $SERVICE_NAME with URL: $SERVICE_URL"
  
  # Create the service in Kong
  curl -s -X PUT \
    --url http://kong:8001/services/$SERVICE_NAME \
    --data "url=$SERVICE_URL" \
    --data "name=$SERVICE_NAME"
  
  # Extract routes for this service
  ROUTES=$(echo $SERVICE | jq -c '.routes[]')
  
  # For each route in the configuration
  for ROUTE in $ROUTES; do
    # Extract route details
    ROUTE_NAME=$(echo $ROUTE | jq -r '.name')
    ROUTE_PATHS=$(echo $ROUTE | jq -r '.paths | join(",")')
    STRIP_PATH=$(echo $ROUTE | jq -r '.strip_path')
    
    echo "Creating route: $ROUTE_NAME for service: $SERVICE_NAME with paths: $ROUTE_PATHS"
    
    # Create the route in Kong
    curl -s -X PUT \
      --url http://kong:8001/services/$SERVICE_NAME/routes/$ROUTE_NAME \
      --data "paths[]=$ROUTE_PATHS" \
      --data "strip_path=$STRIP_PATH" \
      --data "name=$ROUTE_NAME"
  done
done

echo "Kong configuration completed successfully!"