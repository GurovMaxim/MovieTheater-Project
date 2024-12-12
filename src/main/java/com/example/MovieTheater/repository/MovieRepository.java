package com.example.MovieTheater.repository;

import com.example.MovieTheater.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
