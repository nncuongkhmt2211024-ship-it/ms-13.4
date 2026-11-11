package cuong.com.identityservice.dto;

import java.util.UUID;

// DTO trả dữ liệu đầu ra (không chứa password)
public record UserResponse(UUID id, String username, String role) {}