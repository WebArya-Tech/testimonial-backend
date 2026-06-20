#!/bin/bash
set -e

echo "🚀 Starting deployment..."

git fetch origin
git pull

docker compose up -d --build

echo "✅ Deployment done. Showing logs..."
docker compose logs -f