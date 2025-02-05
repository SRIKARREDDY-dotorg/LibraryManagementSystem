package com.srikar.library.user;

import com.srikar.library.Book;
import com.srikar.library.dto.BookCreateRequest;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for handling user-related operations
 */
@RestController
@RequestMapping("/api/users")
public class AdminController extends UserController {
    @Autowired
    private AdminService adminService;

    // Override the parent class methods you want to customize
    @Override
    @GetMapping("/{userId}/books")
    public ResponseEntity<?> viewBooks(@PathVariable String userId) {
        // Add admin-specific logic here if needed
        return super.viewBooks(userId);
    }
    @Override
    @PostMapping("/{userId}/books/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(
            @PathVariable String userId,
            @PathVariable String bookId) {
        // Add admin-specific logic here if needed
        return super.borrowBook(userId, bookId);
    }
    @Override
    @PostMapping("/{userId}/books/return")
    public ResponseEntity<?> returnBooks(
            @PathVariable String userId,
            @RequestBody List<String> bookIds) {
        // Add admin-specific logic here if needed
        return super.returnBooks(userId, bookIds);
    }
    @PostMapping("/{userId}/addbook")
    public ResponseEntity<?> addBook(@PathVariable String userId, @RequestBody BookCreateRequest request) {
        try{
            return ResponseEntity.ok(adminService.addBook(userId, request.getTitle(), request.getAuthor(), request.getStock()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    @GetMapping("/{userId}/checkInventory")
    public ResponseEntity<?> checkInventory(@PathVariable String userId) {
        try{
            return ResponseEntity.ok(adminService.checkInventory(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    @GetMapping("/{userId}/checkBorrowed")
    public ResponseEntity<?> checkBorrowed(@PathVariable String userId) {
        try{
            return ResponseEntity.ok(adminService.checkBorrowedBooks(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
