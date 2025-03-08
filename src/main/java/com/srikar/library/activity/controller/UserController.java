package com.srikar.library.activity.controller;

import com.srikar.library.annotations.AuthenticatedUser;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.activity.service.UserService;
import com.srikar.library.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class for handling user-related operations
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
@AuthenticatedUser
public abstract class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    static class BorrowRequest {
        public String bookId;
    }
    /**
     * View all books in library
     * @return
     */
    @GetMapping("/books")
    public ResponseEntity<?> viewBooks() {
        try {
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
     * @return
     */
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(
            @RequestBody BorrowRequest request) {
        try {
            String userId = jwtUtil.getAuthenticatedEmail();
            boolean borrowed = userService.borrowBook(userId, request.bookId);
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
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    /**
     * Return one or more books to the library
     * @param request
     * @return
     */
    @PostMapping("/return")
    public ResponseEntity<?> returnBooks(@RequestBody BorrowRequest request) {
        try {
            String userId = jwtUtil.getAuthenticatedEmail();
            boolean returned = userService.returnBooks(userId, request.bookId);
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
