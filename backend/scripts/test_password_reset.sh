#!/bin/bash

API_URL="http://localhost:8080/api"
TEST_EMAIL="testuser@example.com"

echo "=== Testing Password Reset Flow ==="

echo "1. Initiating password reset..."
curl -s -X POST "$API_URL/auth/password-reset/initiate" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$TEST_EMAIL\"}"
echo -e "\n"

echo "2. Please check your email for the verification code and enter it:"
read -p "Verification Code: " VERIFICATION_CODE

echo "3. Verifying code and getting reset token..."
VERIFY_RESPONSE=$(curl -s -X POST "$API_URL/auth/password-reset/verify-code" \
  -H "Content-Type: application/json" \
  -d "{\"verificationCode\":\"$VERIFICATION_CODE\"}")

echo "$VERIFY_RESPONSE"
RESET_TOKEN=$(echo "$VERIFY_RESPONSE" | grep -oP '(?<="resetToken":")[^"]+')

if [ -z "$RESET_TOKEN" ]; then
  echo "Failed to get reset token"
  exit 1
fi

echo "4. Resetting password..."
curl -s -X POST "$API_URL/auth/password-reset/reset" \
  -H "Content-Type: application/json" \
  -d "{\"resetToken\":\"$RESET_TOKEN\",\"newPassword\":\"NewPassword123\"}"
echo -e "\n"

echo "Password reset flow completed!"
