package com.example.blog.Controllers;

import com.example.blog.Models.Account;
import com.example.blog.Services.AccountService;
import com.example.blog.repo.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
    @GetMapping("/profile")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Account> getAccount(Authentication authentication) {
        Account account = accountService.getByUsername(authentication.getName());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
