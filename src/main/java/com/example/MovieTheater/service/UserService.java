package com.example.MovieTheater.service;


import com.example.MovieTheater.dto.UserRegistrationDTO;
import com.example.MovieTheater.model.User;
import com.example.MovieTheater.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user with the role "CUSTOMER"
    public String registerUser(UserRegistrationDTO userRegistrationDTO) {
        // Create a new User entity
        User user = new User();
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());

        // Default role is "CUSTOMER"
        user.setRole("CUSTOMER");

        // Save the user to the database
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String createAdmin(UserRegistrationDTO userRegistrationDTO) {
        // Create a new User entity for admin
        User admin = new User();
        admin.setFirstName(userRegistrationDTO.getFirstName());
        admin.setLastName(userRegistrationDTO.getLastName());
        admin.setEmail(userRegistrationDTO.getEmail());
        admin.setPassword(userRegistrationDTO.getPassword());

        // Role is set to "ADMIN"
        admin.setRole("ADMIN");

        // Save the admin to the database
        userRepository.save(admin);
        return "Admin created successfully!";
    }
}