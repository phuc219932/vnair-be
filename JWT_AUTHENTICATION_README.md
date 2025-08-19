# JWT Authentication Implementation

## Tổng quan

Đã implement thành công luồng authentication đơn giản với JWT (JSON Web Token) cho ứng dụng Spring Boot. Người dùng có thể đăng nhập bằng username/password và nhận về JWT token có thời hạn 1 ngày (86400 seconds).

## Các thành phần đã implement

### 1. Dependencies (pom.xml)
- `jjwt-api`: JWT API
- `jjwt-impl`: JWT implementation  
- `jjwt-jackson`: JWT Jackson support

### 2. Configuration (application.properties)
```properties
# JWT Configuration  
jwt.secret=vnair-jwt-secret-key-2024-very-long-string-for-security
jwt.expiration=86400000  # 1 day in milliseconds
```

### 3. JWT Utility (JwtUtils.java)
- Generate JWT tokens với username và userId
- Validate JWT tokens
- Extract thông tin từ tokens (username, userId)
- Token có thời hạn 1 ngày

### 4. Security Configuration (SecurityConfig.java)  
- Stateless session management
- JWT Authentication Filter
- Public endpoints: `/auth/**`, `/users/**`, Swagger UI
- Protected endpoints: `/profile/**` yêu cầu authentication

### 5. Authentication Service (AuthService.java)
- Authenticate user với username/password  
- Verify password bằng BCrypt
- Check user status (ACTIVE)
- Generate JWT token cho user hợp lệ

### 6. DTOs
- `LoginRequestDTO`: Username và password cho login
- `LoginResponseDTO`: Token, user info, expiration time

### 7. Controllers
- `AuthController`: `/auth/login` endpoint
- `ProfileController`: `/profile/me` protected endpoint để test

### 8. Security Filter (JwtAuthenticationFilter)
- Parse JWT token từ Authorization header  
- Validate token và set authentication context
- Chạy trước mỗi request

## API Endpoints

### Authentication
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "adminuser", 
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "adminuser",
  "userId": 5,
  "expiresIn": 86400
}
```

### Protected Endpoint  
```
GET /api/profile/me
Authorization: Bearer <jwt-token>

Response:
{
  "username": "adminuser",
  "message": "Hello adminuser! This is a protected endpoint."
}
```

## Cách sử dụng

1. **Đăng nhập**: POST request đến `/api/auth/login` với username/password
2. **Nhận token**: Server trả về JWT token có thời hạn 1 ngày
3. **Sử dụng token**: Gửi token trong Authorization header: `Bearer <token>`
4. **Access protected endpoints**: Các endpoint protected sẽ check token validity

## Test Script

Chạy `test-auth.ps1` để test toàn bộ authentication flow:
- Login với credentials hợp lệ
- Access protected endpoint với valid token
- Reject access without token
- Reject access với invalid token  
- Reject invalid credentials
- Reject non-existent users

## Security Features

- ✅ JWT tokens có thời hạn 1 ngày
- ✅ Passwords được hash bằng BCrypt  
- ✅ Stateless authentication (không lưu session)
- ✅ Protected endpoints yêu cầu valid JWT
- ✅ Token signature verification
- ✅ User status checking (ACTIVE only)
- ✅ Error handling cho invalid credentials

## User tạo sẵn để test
- Username: `adminuser`
- Password: `password123`
- Status: ACTIVE

Token sẽ expire sau 24 giờ và cần login lại để lấy token mới.
