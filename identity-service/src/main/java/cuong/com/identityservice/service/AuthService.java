package cuong.com.identityservice.service;

import cuong.com.identityservice.dto.UserLoginRequest;
import cuong.com.identityservice.dto.UserRegisterRequest;
import cuong.com.identityservice.dto.UserResponse;
import cuong.com.identityservice.entity.User;
import cuong.com.identityservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cuong.com.identityservice.util.JwtUtil;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // Tiêm JwtUtil vào

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse register(UserRegisterRequest request) {
        // Khởi tạo Entity
        User user = new User();
        user.setUsername(request.username());

        // Băm mật khẩu bằng BCrypt
        user.setPassword(passwordEncoder.encode(request.password()));

        // Gán Role mặc định nếu không truyền
        user.setRole(request.role() != null ? request.role() : "USER");

        // Lưu vào DB
        User savedUser = userRepository.save(user);

        // Trả về DTO không chứa mật khẩu
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }
    public String testToken(String username) {
        // Tìm User trong DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User trong Database"));

        // Tạo và trả về JWT
        return jwtUtil.generateToken(user);
    }

    public String login(UserLoginRequest request) {
        // 1. Tìm User trong Database
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials"));

        // 2. So khớp mật khẩu bằng BCrypt
        boolean isPasswordMatch = passwordEncoder.matches(request.password(), user.getPassword());

        if (!isPasswordMatch) {
            // Ném lỗi 401 Unauthorized với thông báo chung chung
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        // 3. Nếu thành công, tạo và trả về JWT
        return jwtUtil.generateToken(user);
    }
}
