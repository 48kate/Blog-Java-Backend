package com.example.blog.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

 public class AuthenticationManagerImpl implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }
}