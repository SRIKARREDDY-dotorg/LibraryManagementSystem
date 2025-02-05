package com.srikar.library.dto;

/**
 * This class represents a request for creating a new user.
 * It contains the user's name and email address.
 */
public class UserCreateRequest {
    private String name;
    private String email;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

