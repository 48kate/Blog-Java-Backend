package com.example.blog.config;
import com.example.blog.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AccountService accountService;
    @Override
    protected void doFilterInternal(
           @NotNull HttpServletRequest request,
           @NotNull HttpServletResponse response,
           @NotNull FilterChain filterChain // calls next filter in the chain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        // both conditions ok -> send request to next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //delete "Bearer " in the beginning
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername1(jwt);  // extract the username from JWT token;
        // check if username exists in db and user not authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = this.accountService.loadUserByUsername(username);
//                userDetails = this.userDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                response.setHeader("Error", "Account not found");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // check if the user and token is valid or not
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                // extends authToken with details of request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //update security context holder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
