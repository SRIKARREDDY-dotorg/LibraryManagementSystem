package com.srikar.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a request for creating a new user.
 * It contains the user's name and email address.
 */
@Data
@NoArgsConstructor
public class UserCreateRequest {
    private String name;
    private String email;
}
