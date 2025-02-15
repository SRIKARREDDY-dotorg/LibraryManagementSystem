package com.srikar.library.dto;

import lombok.Data;

/**
 * This class represents an authentication request containing email and password.
 */
@Data
public class AuthRequest {
    private String email;
    private String password;
}
