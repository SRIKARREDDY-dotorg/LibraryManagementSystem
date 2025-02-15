package com.srikar.library.activity.controller;

import com.srikar.library.core.User;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.dto.UserCreateRequest;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.activity.service.UserService;
import com.srikar.library.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class for handling user-related operations
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public abstract class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Create a new user
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest request) {
        try {
            User user = userService.createUser(request.getName(), request.getEmail());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    /**
     * View all books in library
     * @return
     */
    @GetMapping("/books")
    public ResponseEntity<?> viewBooks(@RequestHeader ("Authorization") String token) {
        try {
            // Get authenticated user from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Unauthorized"));
            }

            // Extract userId from authenticated UserDetails
            String userId = authentication.getName();  // This comes from JWT
            System.out.println("User ID on books: "+ userId);
            return ResponseEntity.ok(userService.viewBooks());
        } catch (UserNotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    /**
     * Borrow a book from the library
     * @param userId
     * @param bookId
     * @return
     */
    @PostMapping("/{userId}/books/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(
            @PathVariable String userId,
            @PathVariable String bookId) {
        try {
            boolean borrowed = userService.borrowBook(userId, bookId);
            if (borrowed) {
                return ResponseEntity.ok("Book borrowed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Unable to borrow book. due to unknown reason"));
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred"));
        }
    }
    /**
     * Return one or more books to the library
     * @param userId
     * @param bookIds
     * @return
     */
    @PostMapping("/{userId}/books/return")
    public ResponseEntity<?> returnBooks(
            @PathVariable String userId,
            @RequestBody List<String> bookIds) {
        try {
            boolean returned = userService.returnBooks(userId, bookIds);
            if (returned) {
                return ResponseEntity.ok("Books returned successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Unable to return books. due to unknown reason"));
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred"));
        }
    }
}
