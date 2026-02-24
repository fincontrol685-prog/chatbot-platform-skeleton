#!/bin/bash
cd /home/robertojr/chatbot-platform-skeleton/frontend
echo "Starting build..."
ng build --configuration development 2>&1
echo "Build completed with status: $?"

