#!/bin/bash
# Wait for service to be available
# Usage: wait-for-it.sh [HOST] [PORT] [TIMEOUT]

HOST=${1:-localhost}
PORT=${2:-3306}
TIMEOUT=${3:-60}

echo "Aguardando ${HOST}:${PORT} estar disponível (timeout: ${TIMEOUT}s)..."

start_time=$(date +%s)

while true; do
    if timeout 1 bash -c "echo >/dev/tcp/${HOST}/${PORT}" 2>/dev/null; then
        echo "✓ ${HOST}:${PORT} está disponível!"
        exit 0
    fi

    current_time=$(date +%s)
    elapsed=$((current_time - start_time))

    if [ $elapsed -ge $TIMEOUT ]; then
        echo "✗ Timeout esperando ${HOST}:${PORT}"
        exit 1
    fi

    echo -n "."
    sleep 2
done

