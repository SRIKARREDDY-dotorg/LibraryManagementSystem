package com.srikar.library.activity.controller;

import com.srikar.library.core.User;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.dto.UserCreateRequest;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.activity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.srikar.library.core.Book;
import java.util.List;

/**
 * Controller class for handling user-related operations
 */
@RestController
@RequestMapping("/api/users")
public abstract class UserController {

    @Autowired
    private UserService userService;

    /**
     * Create a new user
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        try {
            User user = userService.createUser(request.getName(), request.getEmail());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * View all books in library
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/books")
    public ResponseEntity<?> viewBooks(@PathVariable String userId) {
        try {
            List<Book> books = userService.viewBooks(userId);
            return ResponseEntity.ok(books);
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
