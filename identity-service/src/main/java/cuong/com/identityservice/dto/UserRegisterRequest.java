package cuong.com.identityservice.dto;

// DTO nhận dữ liệu đầu vào
public record UserRegisterRequest(String username, String password, String role) {}
