package com.srikar.library.dto;

import java.time.LocalDateTime;

/**
 * This class represents an error response object that can be used to send error messages
 * back to the client in a structured format.
 */
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
