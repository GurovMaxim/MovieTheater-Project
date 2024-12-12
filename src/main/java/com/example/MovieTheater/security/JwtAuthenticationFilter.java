package com.example.MovieTheater.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            System.out.println("Request URI: " + request.getRequestURI()); // Log URI
            System.out.println("Auth Header: " + authHeader); // Log auth header

            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("No valid auth header found"); // Log missing/invalid header
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            System.out.println("Extracted JWT token"); // Log successful token extraction

            try {
                userEmail = jwtService.extractUsername(jwt);
                System.out.println("Extracted email: " + userEmail); // Log extracted email
            } catch (Exception e) {
                System.out.println("Error extracting email: " + e.getMessage()); // Log extraction error
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("Loaded user details for: " + userEmail); // Log user details loaded

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication successful"); // Log successful authentication
                } else {
                    System.out.println("Token validation failed"); // Log validation failure
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Filter error: " + e.getMessage()); // Log any unexpected errors
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldSkip = path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.startsWith("/api/users/register") ||
                path.startsWith("/api/users/login");

        if (shouldSkip) {
            System.out.println("Skipping JWT filter for path: " + path); // Log skipped paths
        }
        return shouldSkip;
    }
}