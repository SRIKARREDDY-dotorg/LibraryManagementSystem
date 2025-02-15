package com.srikar.library.activity.service;

import com.srikar.library.dao.UserModel;
import com.srikar.library.dao.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is responsible for authenticating users based on their email ID.
 * It implements the UserDetailsService interface from Spring Security.
 */
@Service
public class AuthDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        Optional<UserModel> userOptional = userRepository.findById(emailId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        UserModel user = userOptional.get();

        return User.withUsername(user.getEmail()) // Use email ID
                .password(user.getPassword()) // Store encrypted password
                .roles("USER") // Assign role dynamically if needed
                .build();
    }
}
