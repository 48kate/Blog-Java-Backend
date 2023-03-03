package com.example.blog.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String username;
    private  String password;

}
