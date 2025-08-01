package com.srikar.library.activity.controller;

import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
import com.srikar.library.dto.AuthResponse;
import com.srikar.library.dto.AuthRequest;
import com.srikar.library.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if(request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body("Invalid input");
        }
        if(request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.status(400).body("Invalid input");
        }
        // Fetch user from DB
        Optional<UserModel> userOptional = userRepository.findById(request.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("No Id found in database");
        }
        if(userOptional.isPresent() && !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        UserModel user = userOptional.get();
        final String role = user.getEmail().equals("admin@example.com") ? "ADMIN" : "USER";
        String token = jwtUtil.generateToken(user.getEmail(), role);
        return ResponseEntity.ok(new AuthResponse(token, role));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findById(request.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("User already exists");
        }
        // Create new user
        UserModel user = new UserModel();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password

        userRepository.save(user);
        return ResponseEntity.ok("Successfully Registered");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
            return ResponseEntity.status(400).body("Refresh token is required");
        }
        
        try {
            String email = jwtUtil.extractUsername(request.getRefreshToken());
            
            Optional<UserModel> userOptional = userRepository.findById(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }
            
            UserModel user = userOptional.get();
            final String role = user.getEmail().equals("admin@example.com") ? "ADMIN" : "USER";
            String newToken = jwtUtil.generateToken(user.getEmail(), role);
            
            return ResponseEntity.ok(new AuthResponse(newToken, role));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Normally, invalidate the token
        return ResponseEntity.ok("Logged out successfully");
    }
}

