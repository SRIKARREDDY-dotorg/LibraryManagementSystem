package com.srikar.library.dto;

/**
 * This class represents the response object for authentication.
 * It contains the token generated after successful authentication.
 */
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
