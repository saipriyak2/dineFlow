package com.example.restaurant.Controller;

import com.example.restaurant.Services.UserService;
import com.example.restaurant.dto.request.UserLoginRequest;
import com.example.restaurant.dto.request.UserRegisterRequest;
import com.example.restaurant.dto.response.AuthResponse;
import com.example.restaurant.dto.response.UserResponse;
import com.example.restaurant.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(issueToken(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserResponse user = userService.authenticate(request);
        return ResponseEntity.ok(issueToken(user));
    }

    private AuthResponse issueToken(UserResponse user) {
        String token = jwtService.generateToken(user.id(), user.email(), user.role());
        return AuthResponse.of(token, jwtService.getExpirationSeconds(), user);
    }
}
