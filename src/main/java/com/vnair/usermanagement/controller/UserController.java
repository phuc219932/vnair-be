package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.UserCreateRequestDTO;
import com.vnair.usermanagement.dto.UserResponseDTO;
import com.vnair.usermanagement.dto.UserUpdateRequestDTO;
import com.vnair.usermanagement.entity.UserStatus;
import com.vnair.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "APIs for managing users in the system")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(summary = "Create a new user", 
               description = """
                   <h3>📝 Create a New User Account</h3>
                   <p>Creates a new user account in the system with comprehensive validation and automatic password generation.</p>
                   
                   <h4>🔧 Features:</h4>
                   <ul>
                     <li>✅ <strong>Automatic Password Generation:</strong> If no password provided, generates a secure 6-character random password</li>
                     <li>✅ <strong>Duplicate Prevention:</strong> Automatically checks for existing username, email, and phone</li>
                     <li>✅ <strong>Password Security:</strong> All passwords are encrypted using BCrypt hashing</li>
                     <li>✅ <strong>Flexible Input:</strong> Only username and email are mandatory</li>
                   </ul>
                   
                   <h4>📋 Validation Rules:</h4>
                   <table border="1" style="border-collapse: collapse; width: 100%;">
                     <tr><th>Field</th><th>Required</th><th>Validation</th><th>Default</th></tr>
                     <tr><td><code>username</code></td><td>✅ Yes</td><td>6-50 characters, unique</td><td>-</td></tr>
                     <tr><td><code>email</code></td><td>✅ Yes</td><td>Valid email format, unique</td><td>-</td></tr>
                     <tr><td><code>password</code></td><td>❌ No</td><td>Min 6 chars if provided</td><td>Auto-generated</td></tr>
                     <tr><td><code>phone</code></td><td>❌ No</td><td>Digits/+()-spaces only</td><td>Empty string</td></tr>
                     <tr><td><code>status</code></td><td>❌ No</td><td>ACTIVE/INACTIVE/SUSPENDED/DELETED</td><td>ACTIVE</td></tr>
                   </table>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "✅ **User created successfully** - Returns the created user data with encrypted password hash",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "❌ **Invalid input data**<br/>• Username must be 6-50 characters<br/>• Email must be valid format<br/>• Password must be at least 6 characters (if provided)<br/>• Phone must contain only valid characters"),
        @ApiResponse(responseCode = "409", description = "⚠️ **Conflict - Resource already exists**<br/>• Username already taken<br/>• Email already registered<br/>• Phone number already in use")
    })
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody @Parameter(
                description = """
                    <h4>📄 User Creation Request Body</h4>
                    <p><strong>Content-Type:</strong> <code>application/json</code></p>
                    
                    <h5>📝 Required Fields:</h5>
                    <ul>
                      <li><code>username</code> - Unique identifier (6-50 chars)</li>
                      <li><code>email</code> - Valid email address</li>
                    </ul>
                    
                    <h5>🔧 Optional Fields:</h5>
                    <ul>
                      <li><code>password</code> - User password (auto-generated if omitted)</li>
                      <li><code>phone</code> - Contact number (empty if omitted)</li>
                      <li><code>status</code> - Account status (ACTIVE if omitted)</li>
                    </ul>
                    
                    <h5>💡 Example:</h5>
                    <pre><code>{
  "username": "john_doe_2023",
  "email": "john.doe@example.com",
  "phone": "+84-123-456-789",
  "password": "mySecurePass",
  "status": "ACTIVE"
}</code></pre>
                    """
            ) UserCreateRequestDTO createRequest) {
        logger.info("POST /users - Creating new user");
        UserResponseDTO createdUser = userService.createUser(createRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", 
               description = """
                   <h3>🔍 Retrieve User by ID</h3>
                   <p>Fetches a specific user account using their unique database identifier.</p>
                   
                   <h4>🎯 Use Cases:</h4>
                   <ul>
                     <li>📋 Display user profile details</li>
                     <li>🔧 Administrative user management</li>
                     <li>📊 User data verification</li>
                     <li>🔗 Foreign key relationship lookups</li>
                   </ul>
                   
                   <h4>📝 Response Information:</h4>
                   <p>Returns complete user information excluding sensitive data like password hash.</p>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ **User found successfully**<br/>Returns complete user profile data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "❌ **User not found**<br/>No user exists with the provided ID")
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable @Parameter(
                description = """
                    <h4>🆔 User ID Parameter</h4>
                    <p><strong>Type:</strong> <code>Long (64-bit integer)</code></p>
                    <p><strong>Location:</strong> URL Path Parameter</p>
                    
                    <h5>📋 Details:</h5>
                    <ul>
                      <li><strong>Format:</strong> Positive integer number</li>
                      <li><strong>Example:</strong> <code>1, 42, 12345</code></li>
                      <li><strong>Validation:</strong> Must be a valid database ID</li>
                      <li><strong>Range:</strong> 1 to 9,223,372,036,854,775,807</li>
                    </ul>
                    
                    <h5>🌐 URL Example:</h5>
                    <code>GET /api/users/123</code>
                    """,
                example = "123"
            ) Long id) {
        logger.info("GET /users/{} - Fetching user by ID", id);
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", 
               description = """
                   <h3>👤 Retrieve User by Username</h3>
                   <p>Fetches a specific user account using their unique username identifier.</p>
                   
                   <h4>🎯 Common Use Cases:</h4>
                   <ul>
                     <li>🔐 <strong>Login Verification:</strong> Validate user existence during authentication</li>
                     <li>👤 <strong>Profile Lookup:</strong> Display user information by username</li>
                     <li>🔍 <strong>User Search:</strong> Find specific user accounts</li>
                     <li>🤝 <strong>Social Features:</strong> User mentions and references</li>
                   </ul>
                   
                   <h4>⚡ Performance Notes:</h4>
                   <ul>
                     <li>Database indexed for fast lookups</li>
                     <li>Case-sensitive exact match</li>
                     <li>Returns complete profile data</li>
                   </ul>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ **User found successfully**<br/>Returns complete user profile information"),
        @ApiResponse(responseCode = "404", description = "❌ **User not found**<br/>No user exists with the provided username")
    })
    public ResponseEntity<UserResponseDTO> getUserByUsername(
            @PathVariable @Parameter(
                description = """
                    <h4>👤 Username Parameter</h4>
                    <p><strong>Type:</strong> <code>String</code></p>
                    <p><strong>Location:</strong> URL Path Parameter</p>
                    
                    <h5>📋 Specifications:</h5>
                    <ul>
                      <li><strong>Format:</strong> 6-50 character string</li>
                      <li><strong>Case Sensitivity:</strong> Exact match required</li>
                      <li><strong>Characters:</strong> Letters, numbers, underscores typically allowed</li>
                      <li><strong>Example:</strong> <code>john_doe, user123, admin_user</code></li>
                    </ul>
                    
                    <h5>🌐 URL Examples:</h5>
                    <ul>
                      <li><code>GET /api/users/username/john_doe</code></li>
                      <li><code>GET /api/users/username/admin_2023</code></li>
                    </ul>
                    
                    <h5>⚠️ Important Notes:</h5>
                    <ul>
                      <li>Username must be exactly as stored in database</li>
                      <li>Special characters in URL should be encoded</li>
                      <li>Returns 404 if username doesn't exist</li>
                    </ul>
                    """,
                example = "john_doe"
            ) String username) {
        logger.info("GET /users/username/{} - Fetching user by username", username);
        UserResponseDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        logger.info("GET /users/email/{} - Fetching user by email", email);
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    @Operation(summary = "Get all users with pagination", 
               description = """
                   <h3>📄 Retrieve All Users (Paginated)</h3>
                   <p>Fetches all users from the system with advanced pagination, sorting, and filtering capabilities.</p>
                   
                   <h4>🚀 Features:</h4>
                   <ul>
                     <li>📄 <strong>Pagination Support:</strong> Efficiently handle large datasets</li>
                     <li>🔀 <strong>Flexible Sorting:</strong> Sort by any field in ascending/descending order</li>
                     <li>⚡ <strong>High Performance:</strong> Database-level pagination for optimal speed</li>
                     <li>📊 <strong>Metadata Included:</strong> Total counts, page info, and navigation details</li>
                   </ul>
                   
                   <h4>📋 Sorting Options:</h4>
                   <p>Available sort fields: <code>id</code>, <code>username</code>, <code>email</code>, <code>phone</code>, <code>status</code>, <code>createdAt</code>, <code>updatedAt</code></p>
                   
                   <h4>📊 Response Structure:</h4>
                   <p>Returns a paginated response with content array and pagination metadata.</p>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ **Users retrieved successfully**<br/>Returns paginated user list with metadata")
    })
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") @Parameter(
                description = """
                    <h4>📄 Page Number</h4>
                    <p><strong>Type:</strong> <code>Integer</code> | <strong>Default:</strong> <code>0</code></p>
                    
                    <h5>📋 Details:</h5>
                    <ul>
                      <li><strong>Format:</strong> Zero-based page index</li>
                      <li><strong>Range:</strong> 0 to unlimited</li>
                      <li><strong>Example:</strong> <code>0</code> (first page), <code>1</code> (second page)</li>
                      <li><strong>Behavior:</strong> If page exceeds available pages, returns empty result</li>
                    </ul>
                    
                    <h5>💡 Usage Examples:</h5>
                    <ul>
                      <li><code>?page=0</code> - First page</li>
                      <li><code>?page=5</code> - Sixth page</li>
                    </ul>
                    """,
                example = "0"
            ) int page,
            @RequestParam(defaultValue = "10") @Parameter(
                description = """
                    <h4>📏 Page Size</h4>
                    <p><strong>Type:</strong> <code>Integer</code> | <strong>Default:</strong> <code>10</code></p>
                    
                    <h5>📋 Details:</h5>
                    <ul>
                      <li><strong>Format:</strong> Number of records per page</li>
                      <li><strong>Range:</strong> 1 to 100 (recommended max)</li>
                      <li><strong>Example:</strong> <code>10, 20, 50</code></li>
                      <li><strong>Performance:</strong> Larger sizes may impact response time</li>
                    </ul>
                    
                    <h5>🎯 Common Values:</h5>
                    <ul>
                      <li><code>10</code> - Standard pagination</li>
                      <li><code>20</code> - Medium pagination</li>
                      <li><code>50</code> - Large pagination</li>
                      <li><code>100</code> - Maximum recommended</li>
                    </ul>
                    """,
                example = "10"
            ) int size,
            @RequestParam(defaultValue = "id") @Parameter(
                description = """
                    <h4>🔀 Sort Field</h4>
                    <p><strong>Type:</strong> <code>String</code> | <strong>Default:</strong> <code>id</code></p>
                    
                    <h5>📋 Available Fields:</h5>
                    <table border="1" style="border-collapse: collapse;">
                      <tr><th>Field</th><th>Description</th><th>Type</th></tr>
                      <tr><td><code>id</code></td><td>Database primary key</td><td>Numeric</td></tr>
                      <tr><td><code>username</code></td><td>User's login name</td><td>Text</td></tr>
                      <tr><td><code>email</code></td><td>Email address</td><td>Text</td></tr>
                      <tr><td><code>phone</code></td><td>Phone number</td><td>Text</td></tr>
                      <tr><td><code>status</code></td><td>Account status</td><td>Enum</td></tr>
                      <tr><td><code>createdAt</code></td><td>Creation timestamp</td><td>DateTime</td></tr>
                      <tr><td><code>updatedAt</code></td><td>Last update timestamp</td><td>DateTime</td></tr>
                    </table>
                    
                    <h5>💡 Examples:</h5>
                    <ul>
                      <li><code>?sortBy=username</code> - Sort by username</li>
                      <li><code>?sortBy=createdAt</code> - Sort by creation date</li>
                    </ul>
                    """,
                example = "id"
            ) String sortBy,
            @RequestParam(defaultValue = "asc") @Parameter(
                description = """
                    <h4>↕️ Sort Direction</h4>
                    <p><strong>Type:</strong> <code>String</code> | <strong>Default:</strong> <code>asc</code></p>
                    
                    <h5>📋 Options:</h5>
                    <ul>
                      <li><code><strong>asc</strong></code> - Ascending order (A→Z, 1→9, oldest→newest)</li>
                      <li><code><strong>desc</strong></code> - Descending order (Z→A, 9→1, newest→oldest)</li>
                    </ul>
                    
                    <h5>🎯 Use Cases:</h5>
                    <ul>
                      <li><code>asc</code> with <code>username</code> - Alphabetical order</li>
                      <li><code>desc</code> with <code>createdAt</code> - Newest users first</li>
                      <li><code>asc</code> with <code>id</code> - Original creation order</li>
                    </ul>
                    
                    <h5>💡 Examples:</h5>
                    <ul>
                      <li><code>?sortDir=asc</code> - Ascending</li>
                      <li><code>?sortDir=desc</code> - Descending</li>
                    </ul>
                    """,
                example = "asc"
            ) String sortDir) {
        
        logger.info("GET /users - Fetching all users with pagination");
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByStatus(
            @PathVariable UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /users/status/{} - Fetching users by status", status);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponseDTO> users = userService.getUsersByStatus(status, pageable);
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search users", 
               description = """
                   <h3>🔍 Advanced User Search</h3>
                   <p>Performs intelligent search across multiple user fields with pagination support.</p>
                   
                   <h4>🎯 Search Capabilities:</h4>
                   <ul>
                     <li>🔤 <strong>Username Search:</strong> Partial and exact matches</li>
                     <li>📧 <strong>Email Search:</strong> Domain and address matching</li>
                     <li>📞 <strong>Phone Search:</strong> Number pattern matching</li>
                     <li>🔀 <strong>Multi-field:</strong> Single keyword searches all fields</li>
                   </ul>
                   
                   <h4>🚀 Features:</h4>
                   <ul>
                     <li>⚡ <strong>Fast Search:</strong> Database-optimized queries</li>
                     <li>🎯 <strong>Case Insensitive:</strong> Search regardless of case</li>
                     <li>📄 <strong>Paginated Results:</strong> Handle large result sets</li>
                     <li>🔀 <strong>Sortable:</strong> Order results by any field</li>
                   </ul>
                   
                   <h4>💡 Search Examples:</h4>
                   <ul>
                     <li><code>john</code> - Finds users with "john" in username, email, or phone</li>
                     <li><code>@gmail.com</code> - Finds all Gmail users</li>
                     <li><code>123</code> - Finds users with "123" in phone or username</li>
                   </ul>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ **Search completed successfully**<br/>Returns matching users with pagination metadata")
    })
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam @Parameter(
                description = """
                    <h4>🔍 Search Keyword</h4>
                    <p><strong>Type:</strong> <code>String</code> | <strong>Required:</strong> ✅ Yes</p>
                    
                    <h5>🎯 Search Behavior:</h5>
                    <ul>
                      <li><strong>Fields Searched:</strong> username, email, phone</li>
                      <li><strong>Match Type:</strong> Partial matching (contains)</li>
                      <li><strong>Case Sensitivity:</strong> Case-insensitive</li>
                      <li><strong>Special Characters:</strong> Supported</li>
                    </ul>
                    
                    <h5>💡 Example Keywords:</h5>
                    <table border="1" style="border-collapse: collapse;">
                      <tr><th>Keyword</th><th>Matches</th><th>Use Case</th></tr>
                      <tr><td><code>john</code></td><td>john_doe, johnny123, john@email.com</td><td>Find by name</td></tr>
                      <tr><td><code>@gmail</code></td><td>user@gmail.com, test@gmail.org</td><td>Find by email domain</td></tr>
                      <tr><td><code>+84</code></td><td>+84-123-456, +84-987-654</td><td>Find by country code</td></tr>
                      <tr><td><code>admin</code></td><td>admin_user, administrator</td><td>Find admin accounts</td></tr>
                    </table>
                    
                    <h5>⚠️ Important Notes:</h5>
                    <ul>
                      <li>Minimum 1 character required</li>
                      <li>Whitespace will be trimmed</li>
                      <li>SQL injection protection enabled</li>
                    </ul>
                    """,
                example = "john"
            ) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /users/search - Searching users with keyword: {}", keyword);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponseDTO> users = userService.searchUsers(keyword, pageable);
        
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", 
               description = """
                   <h3>✏️ Update User Information</h3>
                   <p>Updates an existing user account with new information while maintaining data integrity.</p>
                   
                   <h4>🔧 Update Features:</h4>
                   <ul>
                     <li>📝 <strong>Partial Updates:</strong> Only provided fields are updated</li>
                     <li>🛡️ <strong>Validation:</strong> All data validated before saving</li>
                     <li>🔐 <strong>Password Security:</strong> New passwords are encrypted</li>
                     <li>🔄 <strong>Conflict Detection:</strong> Prevents duplicate usernames/emails</li>
                   </ul>
                   
                   <h4>⚠️ Important Rules:</h4>
                   <ul>
                     <li>Username and email must remain unique across system</li>
                     <li>Password updates trigger re-encryption</li>
                     <li>Status changes may affect user access</li>
                     <li>All validation rules from creation apply</li>
                   </ul>
                   
                   <h4>🕐 Timestamps:</h4>
                   <p>The <code>updatedAt</code> timestamp is automatically updated when any changes are made.</p>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ **User updated successfully**<br/>Returns updated user data with new information"),
        @ApiResponse(responseCode = "404", description = "❌ **User not found**<br/>No user exists with the provided ID"),
        @ApiResponse(responseCode = "400", description = "❌ **Invalid input data**<br/>• Field validation failed<br/>• Data format incorrect<br/>• Required constraints not met")
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable @Parameter(
                description = """
                    <h4>🆔 User ID to Update</h4>
                    <p><strong>Type:</strong> <code>Long (64-bit integer)</code></p>
                    <p><strong>Location:</strong> URL Path Parameter</p>
                    
                    <h5>📋 Requirements:</h5>
                    <ul>
                      <li><strong>Format:</strong> Positive integer</li>
                      <li><strong>Validation:</strong> Must exist in database</li>
                      <li><strong>Example:</strong> <code>123, 456, 789</code></li>
                    </ul>
                    
                    <h5>🌐 URL Example:</h5>
                    <code>PUT /api/users/123</code>
                    """,
                example = "123"
            ) Long id, 
            @Valid @RequestBody @Parameter(
                description = """
                    <h4>📄 User Update Request Body</h4>
                    <p><strong>Content-Type:</strong> <code>application/json</code></p>
                    
                    <h5>📝 Updatable Fields:</h5>
                    <table border="1" style="border-collapse: collapse; width: 100%;">
                      <tr><th>Field</th><th>Type</th><th>Validation</th><th>Notes</th></tr>
                      <tr><td><code>username</code></td><td>String</td><td>6-50 chars, unique</td><td>Must be unique</td></tr>
                      <tr><td><code>email</code></td><td>String</td><td>Valid email, unique</td><td>Must be unique</td></tr>
                      <tr><td><code>phone</code></td><td>String</td><td>Phone format</td><td>Optional</td></tr>
                      <tr><td><code>password</code></td><td>String</td><td>Min 6 characters</td><td>Will be encrypted</td></tr>
                      <tr><td><code>status</code></td><td>Enum</td><td>Valid status value</td><td>Affects access</td></tr>
                    </table>
                    
                    <h5>💡 Example Request:</h5>
                    <pre><code>{
  "username": "updated_username",
  "email": "newemail@example.com",
  "phone": "+84-987-654-321",
  "password": "newSecurePassword",
  "status": "ACTIVE"
}</code></pre>
                    
                    <h5>⚠️ Notes:</h5>
                    <ul>
                      <li>Only include fields you want to update</li>
                      <li>All provided fields will be validated</li>
                      <li>Password will be re-encrypted if provided</li>
                    </ul>
                    """
            ) UserUpdateRequestDTO updateRequest) {
        
        logger.info("PUT /users/{} - Updating user", id);
        UserResponseDTO updatedUser = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> statusRequest) {
        
        logger.info("PATCH /users/{}/status - Updating user status", id);
        
        UserStatus status = UserStatus.valueOf(statusRequest.get("status").toUpperCase());
        UserResponseDTO updatedUser = userService.updateUserStatus(id, status);
        
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", 
               description = """
                   <h3>🗑️ Delete User Account</h3>
                   <p>Permanently removes a user account from the system.</p>
                   
                   <h4>⚠️ Important Warning:</h4>
                   <div style="background-color: #fff3cd; padding: 10px; border-left: 4px solid #ffc107;">
                     <p><strong>🚨 DESTRUCTIVE ACTION:</strong> This operation permanently deletes the user account and cannot be undone!</p>
                   </div>
                   
                   <h4>🔍 What Gets Deleted:</h4>
                   <ul>
                     <li>👤 User profile information</li>
                     <li>🔐 Authentication credentials</li>
                     <li>📊 User metadata (creation/update timestamps)</li>
                     <li>🔗 User relationships (may affect foreign key constraints)</li>
                   </ul>
                   
                   <h4>💡 Alternative Approaches:</h4>
                   <ul>
                     <li>🚫 <strong>Soft Delete:</strong> Consider setting status to 'DELETED' instead</li>
                     <li>🔒 <strong>Deactivation:</strong> Set status to 'INACTIVE' to preserve data</li>
                     <li>⏸️ <strong>Suspension:</strong> Set status to 'SUSPENDED' for temporary disable</li>
                   </ul>
                   
                   <h4>🛡️ Pre-deletion Checks:</h4>
                   <p>Ensure the user exists before attempting deletion to avoid errors.</p>
                   """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "✅ **User deleted successfully**<br/>Returns confirmation message with deleted user ID"),
        @ApiResponse(responseCode = "404", description = "❌ **User not found**<br/>No user exists with the provided ID")
    })
    public ResponseEntity<Map<String, String>> deleteUser(
            @PathVariable @Parameter(
                description = """
                    <h4>🆔 User ID to Delete</h4>
                    <p><strong>Type:</strong> <code>Long (64-bit integer)</code></p>
                    <p><strong>Location:</strong> URL Path Parameter</p>
                    
                    <div style="background-color: #f8d7da; padding: 10px; border-left: 4px solid #dc3545; margin: 10px 0;">
                      <p><strong>⚠️ CAUTION:</strong> This user will be permanently deleted!</p>
                    </div>
                    
                    <h5>📋 Requirements:</h5>
                    <ul>
                      <li><strong>Format:</strong> Positive integer number</li>
                      <li><strong>Validation:</strong> Must exist in database</li>
                      <li><strong>Example:</strong> <code>123, 456, 789</code></li>
                      <li><strong>Range:</strong> 1 to 9,223,372,036,854,775,807</li>
                    </ul>
                    
                    <h5>🌐 URL Example:</h5>
                    <code>DELETE /api/users/123</code>
                    
                    <h5>📊 Response Format:</h5>
                    <pre><code>{
  "message": "User deleted successfully",
  "id": "123"
}</code></pre>
                    
                    <h5>💡 Best Practices:</h5>
                    <ul>
                      <li>Always confirm user existence first</li>
                      <li>Consider soft delete alternatives</li>
                      <li>Backup critical data before deletion</li>
                      <li>Check for dependent records</li>
                    </ul>
                    """,
                example = "123"
            ) Long id) {
        logger.info("DELETE /users/{} - Deleting user", id);
        
        userService.deleteUser(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(@PathVariable String username) {
        logger.info("GET /users/exists/username/{} - Checking if username exists", username);
        
        boolean exists = userService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
        logger.info("GET /users/exists/email/{} - Checking if email exists", email);
        
        boolean exists = userService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getUserStats() {
        logger.info("GET /users/stats - Fetching user statistics");
        
        Map<String, Long> stats = new HashMap<>();
        stats.put("active", userService.countUsersByStatus(UserStatus.ACTIVE));
        stats.put("inactive", userService.countUsersByStatus(UserStatus.INACTIVE));
        stats.put("suspended", userService.countUsersByStatus(UserStatus.SUSPENDED));
        stats.put("deleted", userService.countUsersByStatus(UserStatus.DELETED));
        
        return ResponseEntity.ok(stats);
    }
}
