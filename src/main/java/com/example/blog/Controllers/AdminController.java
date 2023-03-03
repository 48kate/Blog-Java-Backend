package com.example.blog.Controllers;

import com.example.blog.Models.Account;
import com.example.blog.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
public class AdminController {
    @Autowired
    private AccountService accountService;
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @GetMapping("/admin")
    public ResponseEntity<List<Account>> findAllUsers() {
        List<Account> accounts = accountService.allUsers();
        return ResponseEntity.ok(accounts);
    }
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  ResponseEntity<String>  deleteUser(@PathVariable Long id) {
        try {
            accountService.deleteUser(id);
            return new ResponseEntity<>("User was deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
