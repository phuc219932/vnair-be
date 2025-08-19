# User Management REST API

Ứng dụng REST API quản lý user được xây dựng với Spring Boot và PostgreSQL.

## Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **BCrypt** (mã hóa mật khẩu)

## Cấu trúc dự án

```
src/main/java/com/vnair/usermanagement/
├── controller/          # REST Controllers
├── service/            # Business Logic Layer
├── repository/         # Data Access Layer
├── entity/            # JPA Entities
├── dto/               # Data Transfer Objects
├── exception/         # Exception Handling
└── UserManagementApiApplication.java
```

## Cài đặt và chạy ứng dụng

### Cách 1: Chạy với Docker (Khuyên dùng)

#### 1. Cài đặt Docker
- Cài đặt Docker Desktop
- Đảm bảo Docker đang chạy

#### 2. Chạy với Docker Compose
```bash
# Sử dụng script (Windows)
.\docker-run.ps1 start

# Hoặc sử dụng script (Linux/Mac)  
./docker-run.sh start

# Hoặc chạy trực tiếp
docker-compose up --build -d
```

#### 3. Kiểm tra services
```bash
# Kiểm tra trạng thái
docker-compose ps

# Xem logs
docker-compose logs -f app

# Dừng services
docker-compose down
```

### Cách 2: Chạy thủ công

#### 1. Cài đặt PostgreSQL
- Cài đặt PostgreSQL
- Tạo database: `vnair_user_db`
- Cấu hình username/password trong `application.properties`

#### 2. Chạy SQL script
```bash
psql -U postgres -d vnair_user_db -f src/main/resources/database/init.sql
```

#### 3. Chạy ứng dụng
```bash
mvn clean install
mvn spring-boot:run
```

### URLs sau khi chạy:
- **API**: `http://localhost:8080/api`
- **Database**: `localhost:5432`  
- **Adminer** (DB GUI): `http://localhost:8081`

## API Endpoints

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users` | Tạo user mới |
| GET | `/users/{id}` | Lấy user theo ID |
| GET | `/users/username/{username}` | Lấy user theo username |
| GET | `/users/email/{email}` | Lấy user theo email |
| GET | `/users` | Lấy danh sách user (có phân trang) |
| GET | `/users/status/{status}` | Lấy user theo trạng thái |
| GET | `/users/search?keyword={keyword}` | Tìm kiếm user |
| PUT | `/users/{id}` | Cập nhật user |
| PATCH | `/users/{id}/status` | Cập nhật trạng thái user |
| DELETE | `/users/{id}` | Xóa user |
| GET | `/users/exists/username/{username}` | Kiểm tra username tồn tại |
| GET | `/users/exists/email/{email}` | Kiểm tra email tồn tại |
| GET | `/users/stats` | Thống kê user |

### Ví dụ Request/Response

#### Tạo user mới
```bash
POST /api/users
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john@example.com",
    "phone": "+84901234567",
    "password": "password123",
    "status": "ACTIVE"
}
```

#### Response
```json
{
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "phone": "+84901234567",
    "status": "ACTIVE",
    "createdAt": "2025-08-10T10:30:00",
    "updatedAt": "2025-08-10T10:30:00"
}
```

#### Lấy danh sách user với phân trang
```bash
GET /api/users?page=0&size=10&sortBy=username&sortDir=asc
```

#### Tìm kiếm user
```bash
GET /api/users/search?keyword=john&page=0&size=10
```

#### Cập nhật trạng thái user
```bash
PATCH /api/users/1/status
Content-Type: application/json

{
    "status": "INACTIVE"
}
```

## User Status

- `ACTIVE`: Hoạt động
- `INACTIVE`: Không hoạt động
- `SUSPENDED`: Tạm khóa
- `DELETED`: Đã xóa

## Validation Rules

- **Username**: 3-50 ký tự, duy nhất
- **Email**: Format email hợp lệ, duy nhất
- **Phone**: Chỉ chứa số và ký tự +()-space
- **Password**: Tối thiểu 6 ký tự

## Error Handling

API trả về các mã lỗi HTTP chuẩn:
- `200 OK`: Thành công
- `201 Created`: Tạo thành công
- `400 Bad Request`: Validation lỗi
- `404 Not Found`: Không tìm thấy
- `409 Conflict`: Dữ liệu trùng lặp
- `500 Internal Server Error`: Lỗi server

### Ví dụ Error Response
```json
{
    "status": 404,
    "message": "User not found with ID: 999",
    "timestamp": "2025-08-10T10:30:00"
}
```

## Database Schema

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Tính năng

- ✅ CRUD operations cho User
- ✅ Phân trang và sắp xếp
- ✅ Tìm kiếm user
- ✅ Validation dữ liệu
- ✅ Exception handling
- ✅ Mã hóa mật khẩu với BCrypt
- ✅ Kiểm tra trùng lặp
- ✅ Thống kê user theo status
- ✅ RESTful API design
- ✅ Logging

## Cấu hình

### Local Development
Cấu hình database trong `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vnair_user_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Docker Environment  
Cấu hình được quản lý tự động trong `docker-compose.yml` và `application-docker.properties`.

## Docker

### Services
- **app**: Spring Boot API (Port 8080)
- **postgres**: PostgreSQL Database (Port 5432)  
- **adminer**: Database GUI (Port 8081)

### Docker Commands
```bash
# Windows
.\docker-run.ps1 start     # Khởi động tất cả services
.\docker-run.ps1 stop      # Dừng tất cả services  
.\docker-run.ps1 status    # Kiểm tra trạng thái
.\docker-run.ps1 logs      # Xem logs
.\docker-run.ps1 restart   # Restart services
.\docker-run.ps1 clean     # Xóa tất cả Docker resources

# Linux/Mac
./docker-run.sh start      # Khởi động tất cả services
./docker-run.sh stop       # Dừng tất cả services
./docker-run.sh status     # Kiểm tra trạng thái  
./docker-run.sh logs       # Xem logs
./docker-run.sh restart    # Restart services
./docker-run.sh clean      # Xóa tất cả Docker resources
```

### Docker Compose Manual Commands
```bash
# Khởi động services
docker-compose up -d

# Xây dựng lại và khởi động
docker-compose up --build -d

# Dừng services
docker-compose down

# Xem logs
docker-compose logs -f app

# Kiểm tra trạng thái
docker-compose ps
```
