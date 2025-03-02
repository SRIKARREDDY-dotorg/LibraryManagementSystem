package com.srikar.library.dto;

/**
 * This class represents the response object for authentication.
 * It contains the token generated after successful authentication.
 */
public class AuthResponse {
    private String token;
    private String role;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role =  role;
    }
    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
