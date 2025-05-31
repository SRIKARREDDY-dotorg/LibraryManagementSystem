package com.srikar.library.activity.controller;

import com.srikar.library.dto.BookCreateRequest;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.activity.service.AdminService;
import com.srikar.library.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling user-related operations
 */
@RestController
@RequestMapping("/api/users")
public class AdminController extends UserController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtUtil jwtUtil;
    // Override the parent class methods you want to customize
    @Override
    @GetMapping("/books")
    public ResponseEntity<?> viewBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return super.viewBooks(page, size);
    }
    @Override
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(
            @RequestBody BorrowRequest request) {
        // Add admin-specific logic here if needed
        return super.borrowBook(request);
    }
    @Override
    @PostMapping("/return")
    public ResponseEntity<?> returnBooks(
            @RequestBody BorrowRequest request) {
        // Add admin-specific logic here if needed
        return super.returnBooks(request);
    }
    @PostMapping("/add_book")
    public ResponseEntity<?> addBook(@RequestBody BookCreateRequest request) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Access denied"));
        }
        try{
            return ResponseEntity.ok(adminService.addBook(request.getTitle(), request.getAuthor(), request.getStock(), request.getUrl()));
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
    @PostMapping("/update_book")
    public ResponseEntity<?> updateBook(@RequestBody BookCreateRequest request) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Access denied"));
        }
        try{
            return ResponseEntity.ok(adminService.updateBook(request.getId(), request.getTitle(), request.getAuthor(), request.getStock(), request.getUrl()));
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

    @GetMapping("/checkBorrowed")
    public ResponseEntity<?> checkBorrowed() {
        // for admin return for all the users borrowed books including admin,
        // If a user, then only show that users books.
        try{
            return ResponseEntity.ok(adminService.checkBorrowedBooks(isAdmin(), jwtUtil.getAuthenticatedEmail()));
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
    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}
