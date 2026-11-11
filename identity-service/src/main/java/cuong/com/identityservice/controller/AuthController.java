package cuong.com.identityservice.controller;

import cuong.com.identityservice.dto.UserLoginRequest;
import cuong.com.identityservice.dto.AuthResponse;
import cuong.com.identityservice.dto.UserRegisterRequest;
import cuong.com.identityservice.dto.UserResponse;
import cuong.com.identityservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/test-token")
    public ResponseEntity<String> testToken(@RequestParam String username) {
        String token = authService.testToken(username);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}