package com.example.MovieTheater.controller;



import com.example.MovieTheater.dto.BookingRequest;
import com.example.MovieTheater.model.Booking;
import com.example.MovieTheater.model.Seat;
import com.example.MovieTheater.model.User;
import com.example.MovieTheater.repository.BookingRepository;
import com.example.MovieTheater.repository.SeatRepository;
import com.example.MovieTheater.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest, HttpServletRequest request) {
        try {
            System.out.println("Received booking request: " + bookingRequest);
            System.out.println("Auth header: " + request.getHeader("Authorization"));

            // Get seat by row, number AND movie ID
            Seat seat = seatRepository.findBySeatRowAndNumberAndMovieId(
                    bookingRequest.getSeatRow(),
                    bookingRequest.getNumber(),
                    bookingRequest.getMovieId()  // Include movie ID in the lookup
            );

            if (seat == null) {
                System.out.println("Seat not found");
                return ResponseEntity.badRequest().body("Seat not found");
            }

            // Check if seat is already occupied
            if (seat.isOccupied()) {
                System.out.println("Seat is already occupied");
                return ResponseEntity.badRequest().body("Seat is already occupied");
            }

            // Get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Mark the seat as occupied
            seat.setOccupied(true);
            seatRepository.save(seat);

            // Create new booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setSeat(seat);
            booking.setConfirmed(true);

            // Save the booking
            Booking savedBooking = bookingRepository.save(booking);
            System.out.println("Booking saved successfully: " + savedBooking);

            return ResponseEntity.ok(savedBooking);
        } catch (Exception e) {
            System.out.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating booking: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserBookings(HttpServletRequest request) {
        try {
            // Get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            List<Booking> bookings = bookingRepository.findByUserId(user.getId());
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching bookings: " + e.getMessage());
        }
    }
}