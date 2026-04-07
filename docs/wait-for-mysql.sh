#!/bin/sh
# Wait for MySQL to be ready before starting the application

HOST=${DB_HOST:-mysql}
PORT=${DB_PORT:-3306}
MAX_ATTEMPTS=60
ATTEMPT=0

echo "Waiting for MySQL at $HOST:$PORT..."

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    if nc -z -w 1 $HOST $PORT 2>/dev/null; then
        echo "✓ MySQL is ready!"
        exit 0
    fi

    ATTEMPT=$((ATTEMPT + 1))
    echo "Attempt $ATTEMPT/$MAX_ATTEMPTS: MySQL not ready yet, waiting..."
    sleep 2
done

echo "✗ MySQL failed to start after $MAX_ATTEMPTS attempts"
exit 1

