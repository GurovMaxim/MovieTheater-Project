package com.example.MovieTheater.controller;

import com.example.MovieTheater.model.Movie;
import com.example.MovieTheater.model.Seat;
import com.example.MovieTheater.repository.MovieRepository;
import com.example.MovieTheater.repository.SeatRepository;
import com.example.MovieTheater.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private MovieService movieService;

    // Create a new movie
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovieWithSeats(movie);
    }

    // Get all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();  // Return all movies
    }

    // Get movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        try {
            Movie movie = movieRepository.findById(id).orElse(null);
            if (movie == null) {
                return ResponseEntity.notFound().build();
            }

            List<Seat> seats = seatRepository.findByMovieId(id);

            // Create response object with movie details and seats
            Map<String, Object> response = new HashMap<>();
            response.put("id", movie.getId());
            response.put("name", movie.getName());
            response.put("description", movie.getDescription());
            response.put("duration", movie.getDuration());
            response.put("imageName", movie.getImageName());
            response.put("seats", seats);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting movie details: " + e.getMessage());
        }
    }

    @GetMapping("/{movieId}/seats")
    public ResponseEntity<List<Seat>> getMovieSeats(@PathVariable Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return ResponseEntity.ok(seatRepository.findByMovie(movie));
    }

    // Delete movie by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movieRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}