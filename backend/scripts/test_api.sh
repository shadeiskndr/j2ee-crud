#!/bin/bash

API_URL="http://localhost:8080/api"
USERNAME="apitestuser"
EMAIL="apitestuser@example.com"
PASSWORD="SuperSecret123"

echo "=== Registering user ==="
curl -s -X POST "$API_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}"
echo -e "\n"

echo "=== Logging in ==="
LOGIN_RESPONSE=$(curl -s -X POST "$API_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

echo "$LOGIN_RESPONSE"
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -oP '(?<="token":")[^"]+')

if [ -z "$TOKEN" ]; then
  echo "Login failed or token not found."
  exit 1
fi

echo -e "\n=== Accessing protected /api/members endpoint ==="
curl -s -X GET "$API_URL/members" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n"

echo "=== Testing unauthorized access (should fail) ==="
curl -s -X GET "$API_URL/members"
echo -e "\n"
