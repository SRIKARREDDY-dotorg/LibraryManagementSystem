package com.srikar.library.activity.controller;

import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
import com.srikar.library.dto.AuthRequest;
import com.srikar.library.dto.AuthResponse;
import com.srikar.library.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private AuthRequest validRequest;
    private UserModel validUser;

    @BeforeEach
    void setUp() {
        validRequest = new AuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        validUser = new UserModel();
        validUser.setEmail("test@example.com");
        validUser.setPassword("encodedPassword");
    }

    @Test
    void login_WithValidCredentials_ReturnsToken() {
        // Arrange
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com", "USER")).thenReturn("valid-token");

        // Act
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("valid-token", authResponse.getToken());
        assertEquals("USER", authResponse.getRole());
    }

    @Test
    void login_WithAdminCredentials_ReturnsAdminToken() {
        // Arrange
        validRequest.setEmail("admin@example.com");
        validUser.setEmail("admin@example.com");
        
        when(userRepository.findById("admin@example.com")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@example.com", "ADMIN")).thenReturn("admin-token");

        // Act
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("admin-token", authResponse.getToken());
        assertEquals("ADMIN", authResponse.getRole());
    }

    @Test
    void login_WithInvalidPassword_ReturnsUnauthorized() {
        // Arrange
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void login_WithNonexistentUser_ReturnsUnauthorized() {
        // Arrange
        when(userRepository.findById("test@example.com")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("No Id found in database", response.getBody());
    }

    @Test
    void login_WithEmptyPassword_ReturnsBadRequest() {
        // Arrange
        validRequest.setPassword("");

        // Act
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input", response.getBody());
    }

    @Test
    void login_WithEmptyEmail_ReturnsBadRequest() {
        // Arrange
        validRequest.setEmail("");

        // Act
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input", response.getBody());
    }

    @Test
    void register_WithNewUser_ReturnsSuccess() {
        // Arrange
        when(userRepository.findById("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = authController.register(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully Registered", response.getBody());
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    void register_WithExistingUser_ReturnsBadRequest() {
        // Arrange
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(validUser));

        // Act
        ResponseEntity<?> response = authController.register(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void logout_ReturnsSuccess() {
        // Act
        ResponseEntity<?> response = authController.logout();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());
    }
}
