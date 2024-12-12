package com.example.MovieTheater.repository;

import com.example.MovieTheater.model.Movie;
import com.example.MovieTheater.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findBySeatRowAndNumberAndMovieId(String seatRow, int number, Long movieId);
    List<Seat> findByMovieId(Long movieId);
    List<Seat> findByMovie(Movie movie);
}
