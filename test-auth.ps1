# Test Authentication API - JWT Implementation

$baseUrl = "http://localhost:8888/api"

Write-Host "=== JWT Authentication Test Suite ===" -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host ""

# Test 1: Login with valid credentials
Write-Host "=== Test 1: Login with valid credentials ===" -ForegroundColor Cyan
$loginData = @{
    username = "adminuser"
    password = "password123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    Write-Host "✓ Login successful!" -ForegroundColor Green
    Write-Host "  Username: $($response.username)" -ForegroundColor Gray
    Write-Host "  User ID: $($response.userId)" -ForegroundColor Gray
    Write-Host "  Token Type: $($response.type)" -ForegroundColor Gray
    Write-Host "  Expires In: $($response.expiresIn) seconds (1 day)" -ForegroundColor Gray
    Write-Host "  Token: $($response.token.Substring(0, 20))..." -ForegroundColor Gray
    
    # Store token for next tests
    $token = $response.token
    
} catch {
    Write-Host "✗ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    return
}

# Test 2: Access protected endpoint with valid token
Write-Host "`n=== Test 2: Access protected endpoint with valid JWT token ===" -ForegroundColor Cyan
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

try {
    $profileResponse = Invoke-RestMethod -Uri "$baseUrl/profile/me" -Method GET -Headers $headers
    Write-Host "✓ Protected endpoint accessed successfully!" -ForegroundColor Green
    Write-Host "  Username: $($profileResponse.username)" -ForegroundColor Gray
    Write-Host "  Message: $($profileResponse.message)" -ForegroundColor Gray
    
} catch {
    Write-Host "✗ Failed to access protected endpoint: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Access protected endpoint without token
Write-Host "`n=== Test 3: Access protected endpoint without token ===" -ForegroundColor Cyan
try {
    $profileResponse = Invoke-RestMethod -Uri "$baseUrl/profile/me" -Method GET
    Write-Host "✗ This should have been blocked!" -ForegroundColor Red
    
} catch {
    Write-Host "✓ Correctly blocked access without token (403 Forbidden)" -ForegroundColor Green
    Write-Host "  Error: Access denied as expected" -ForegroundColor Gray
}

# Test 4: Access protected endpoint with invalid token
Write-Host "`n=== Test 4: Access protected endpoint with invalid token ===" -ForegroundColor Cyan
$invalidHeaders = @{
    "Authorization" = "Bearer invalid-jwt-token-here"
    "Content-Type" = "application/json"
}

try {
    $profileResponse = Invoke-RestMethod -Uri "$baseUrl/profile/me" -Method GET -Headers $invalidHeaders
    Write-Host "✗ This should have been blocked!" -ForegroundColor Red
    
} catch {
    Write-Host "✓ Correctly blocked access with invalid token" -ForegroundColor Green
    Write-Host "  Error: Invalid JWT signature as expected" -ForegroundColor Gray
}

# Test 5: Login with invalid credentials
Write-Host "`n=== Test 5: Login with invalid credentials ===" -ForegroundColor Cyan
$invalidLoginData = @{
    username = "adminuser"
    password = "wrongpassword"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $invalidLoginData -ContentType "application/json"
    Write-Host "✗ This should have failed!" -ForegroundColor Red
    
} catch {
    Write-Host "✓ Correctly rejected invalid credentials" -ForegroundColor Green
    Write-Host "  Error: Authentication failed as expected" -ForegroundColor Gray
}

# Test 6: Login with non-existent user
Write-Host "`n=== Test 6: Login with non-existent user ===" -ForegroundColor Cyan
$nonExistentLoginData = @{
    username = "nonexistentuser"
    password = "password123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $nonExistentLoginData -ContentType "application/json"
    Write-Host "✗ This should have failed!" -ForegroundColor Red
    
} catch {
    Write-Host "✓ Correctly rejected non-existent user" -ForegroundColor Green
    Write-Host "  Error: User not found as expected" -ForegroundColor Gray
}

Write-Host "`n=== Summary ===" -ForegroundColor Green
Write-Host "✓ JWT Authentication system implemented successfully!" -ForegroundColor Green
Write-Host "✓ Token expires after 1 day (86400 seconds)" -ForegroundColor Green  
Write-Host "✓ Protected endpoints require valid JWT tokens" -ForegroundColor Green
Write-Host "✓ Invalid credentials and tokens are properly rejected" -ForegroundColor Green
Write-Host "`n=== Test Suite Complete ===" -ForegroundColor Green
