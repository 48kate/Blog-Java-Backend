package com.example.blog.auth;
import com.example.blog.Models.Account;
import com.example.blog.Models.Role;
import com.example.blog.Services.AccountService;
import com.example.blog.config.JwtService;
import com.example.blog.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // allow us to create user, save it to the db and return the generated token out of it
    public AuthenticationResponse register(RegisterRequest request) {
        var account = Account.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        String password  =request.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        UserDetails account;
        try {
            account = accountService.loadUserByUsernameAndPassword(
                    username,
                    encodedPassword
            );
        } catch (UsernameNotFoundException e) {
            try {
                account = accountService.loadUserByUsername(
                        username
                );
            } catch (UsernameNotFoundException e2) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username not found");
            }
            if (!passwordEncoder.matches(password, account.getPassword())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password is wrong");
            };
        }
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}
