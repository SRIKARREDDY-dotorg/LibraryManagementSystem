package com.srikar.library.activity.service;

import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthDetailsService authDetailsService;

    @Test
    void testLoadUserByUsernameSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setPassword(password);

        when(userRepository.findById(email)).thenReturn(Optional.of(userModel));

        // Act
        UserDetails userDetails = authDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        // Verify USER role is assigned
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));

        verify(userRepository).findById(email);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(email);
    }

    @Test
    void testLoadUserByUsernameWithAdminEmail() {
        // Arrange
        String email = "admin@example.com";
        String password = "adminpass";
        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setPassword(password);

        when(userRepository.findById(email)).thenReturn(Optional.of(userModel));

        // Act
        UserDetails userDetails = authDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        // Verify USER role is assigned (even for admin email - role assignment happens elsewhere)
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));

        verify(userRepository).findById(email);
    }

    @Test
    void testLoadUserByUsernameWithNullEmail() {
        // Arrange
        when(userRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authDetailsService.loadUserByUsername(null);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
