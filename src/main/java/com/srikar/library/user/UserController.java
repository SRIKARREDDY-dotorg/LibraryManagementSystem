package com.srikar.library.user;

import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.dto.UserCreateRequest;
import com.srikar.library.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.srikar.library.Book;
import java.util.List;

/**
 * Controller class for handling user-related operations
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

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
        } catch (Exception e) {
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
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
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
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
