package com.example.MovieTheater.service;

import com.example.MovieTheater.model.Movie;
import com.example.MovieTheater.model.Seat;
import com.example.MovieTheater.repository.MovieRepository;
import com.example.MovieTheater.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SeatRepository seatRepository;

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // Create a movie and generate seats
    public Movie createMovieWithSeats(Movie movie) {
        // Save the movie first
        Movie savedMovie = movieRepository.save(movie);

        // Generate default seats (Rows A-G, Numbers 1-14)
        List<Seat> seats = new ArrayList<>();
        for (char row = 'A'; row <= 'G'; row++) {
            for (int number = 1; number <= 14; number++) {
                Seat seat = new Seat();
                seat.setSeatRow(String.valueOf(row));
                seat.setNumber(number);
                seat.setOccupied(false);
                seat.setMovie(savedMovie); // Associate the seat with the movie
                seats.add(seat);
            }
        }

        // Save all seats
        seatRepository.saveAll(seats);

        return savedMovie;
    }
}

