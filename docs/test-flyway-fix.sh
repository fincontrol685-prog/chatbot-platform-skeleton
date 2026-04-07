#!/bin/bash

# ============================================================
# Chatbot Platform - Flyway MySQL 8.0 Fix - Test Script
# ============================================================
# This script will rebuild and test the Docker Compose setup
# with the fixed Flyway dependencies.
# ============================================================

set -e

PROJECT_DIR="/home/robertojr/chatbot-platform-skeleton"
cd "$PROJECT_DIR"

echo "========================================="
echo "Flyway MySQL 8.0 Fix - Docker Test"
echo "========================================="
echo ""

# Step 1: Clean and rebuild Maven project
echo "Step 1: Building Maven project with updated Flyway..."
echo "This may take 1-2 minutes..."
./mvnw clean package -DskipTests -q
BUILD_RESULT=$?

if [ $BUILD_RESULT -eq 0 ]; then
    echo "✅ Maven build successful"
else
    echo "❌ Maven build failed"
    exit 1
fi

echo ""

# Step 2: Stop and remove old containers
echo "Step 2: Cleaning up Docker containers and volumes..."
docker-compose down -v 2>/dev/null || true
echo "✅ Docker cleanup done"

echo ""

# Step 3: Rebuild Docker image
echo "Step 3: Building Docker image..."
docker-compose build backend --no-cache -q 2>/dev/null || docker-compose build backend
echo "✅ Docker image built"

echo ""

# Step 4: Start containers
echo "Step 4: Starting Docker Compose services..."
docker-compose up -d
echo "✅ Docker Compose started"

echo ""

# Step 5: Wait for startup
echo "Step 5: Waiting for services to start (60 seconds)..."
sleep 60

echo ""

# Step 6: Check backend logs for errors
echo "Step 6: Checking backend logs..."
echo "========================================="

LOGS=$(docker-compose logs chatbot-backend)

if echo "$LOGS" | grep -q "Unsupported Database: MySQL 8.0"; then
    echo "❌ FAILED: Flyway still reports MySQL 8.0 as unsupported"
    echo ""
    echo "Full logs:"
    echo "$LOGS"
    exit 1
fi

if echo "$LOGS" | grep -q "Root WebApplicationContext: initialization completed"; then
    echo "✅ SUCCESS: Application started successfully!"
    echo ""
    echo "Backend is ready at: http://localhost:8080/api/v1"
    echo ""
    echo "Verify with:"
    echo "  curl http://localhost:8080/api/v1/actuator/health"
    exit 0
else
    echo "⚠️  UNCERTAIN: Could not confirm successful startup"
    echo ""
    echo "Recent logs:"
    echo "$LOGS" | tail -50
    exit 1
fi

