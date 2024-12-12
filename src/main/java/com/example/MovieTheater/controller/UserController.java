package com.example.MovieTheater.controller;

import com.example.MovieTheater.dto.LoginRequest;
import com.example.MovieTheater.dto.UserRegistrationDTO;
import com.example.MovieTheater.model.User;
import com.example.MovieTheater.repository.UserRepository;
import com.example.MovieTheater.security.JwtService;
import com.example.MovieTheater.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;  // Inject JwtService

    @Autowired
    private AuthenticationManager authenticationManager;  // Inject AuthenticationManager

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Register a new user (default role: customer)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        String response = userService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok(response);
    }

    // Login endpoint - Generates and returns a JWT token
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            // Generate JWT token
            String token = jwtService.generateToken(user);  // Now, `user` implements UserDetails
            return ResponseEntity.ok("Bearer " + token);  // Returning the token as a response
        } else {
            return ResponseEntity.status(401).body("Invalid credentials!");
        }
    }

    // Logout endpoint
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidates the user's session and JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        // Clear the security context
        SecurityContextHolder.clearContext();

        // Clear any session data
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    // Create a new admin (Swagger-only access)
    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        String response = userService.createAdmin(userRegistrationDTO);
        return ResponseEntity.ok(response);
    }
}
