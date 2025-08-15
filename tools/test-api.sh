# Test API Script
# Sử dụng curl để test các endpoints

# Base URL
BASE_URL="http://localhost:8080/api"

echo "=== Testing User Management API ==="

# 1. Create a new user
echo "1. Creating new user..."
curl -X POST $BASE_URL/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "phone": "+84901234567",
    "password": "password123"
  }'

echo -e "\n"

# 2. Get all users
echo "2. Getting all users..."
curl -X GET "$BASE_URL/users?page=0&size=5"

echo -e "\n"

# 3. Get user by ID
echo "3. Getting user by ID=1..."
curl -X GET $BASE_URL/users/1

echo -e "\n"

# 4. Search users
echo "4. Searching users with keyword 'test'..."
curl -X GET "$BASE_URL/users/search?keyword=test"

echo -e "\n"

# 5. Update user status
echo "5. Updating user status..."
curl -X PATCH $BASE_URL/users/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "INACTIVE"}'

echo -e "\n"

# 6. Get user statistics
echo "6. Getting user statistics..."
curl -X GET $BASE_URL/users/stats

echo -e "\n"

# 7. Check if username exists
echo "7. Checking if username exists..."
curl -X GET $BASE_URL/users/exists/username/testuser

echo -e "\n"

echo "=== API Testing Complete ==="
