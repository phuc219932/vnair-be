#!/bin/bash

echo "=== Checking available Docker images ==="

echo "Checking Maven images..."
docker search maven --limit 5

echo ""
echo "Pulling recommended images..."

# Test different Maven images
echo "Testing maven:3.8-openjdk-17-slim..."
docker pull maven:3.8-openjdk-17-slim

echo "Testing maven:3.9-openjdk-17..."  
docker pull maven:3.9-openjdk-17

echo "Testing eclipse-temurin:17-jre-alpine..."
docker pull eclipse-temurin:17-jre-alpine

echo "Testing openjdk:17-jre-slim..."
docker pull openjdk:17-jre-slim

echo ""
echo "✅ All images pulled successfully!"
echo ""
echo "You can now use any of these Dockerfiles:"
echo "1. Dockerfile (main) - uses maven:3.9-openjdk-17 + eclipse-temurin:17-jre-alpine"  
echo "2. Dockerfile.alternative - uses maven:3.8-openjdk-17-slim + openjdk:17-jre-slim"
echo ""
echo "To use alternative Dockerfile:"
echo "docker-compose build --build-arg DOCKERFILE=Dockerfile.alternative"
