package com.example.MovieTheater.config;

import com.example.MovieTheater.model.Movie;
import com.example.MovieTheater.model.Seat;
import com.example.MovieTheater.model.User;
import com.example.MovieTheater.repository.MovieRepository;
import com.example.MovieTheater.repository.SeatRepository;
import com.example.MovieTheater.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed Movies
        seedMovies();
        // Seed Test User
        seedTestUser();
    }

    private void seedMovies() {
        // Create and save movies
        Movie darkKnight = new Movie();
        darkKnight.setName("The Dark Knight");
        darkKnight.setDescription("A thrilling action movie where Batman confronts the Joker in Gotham City.");
        darkKnight.setDuration(152);
        darkKnight.setImageName("dark_knight.jpg");
        movieRepository.save(darkKnight);

        Movie budapestHotel = new Movie();
        budapestHotel.setName("The Grand Budapest Hotel");
        budapestHotel.setDescription("A quirky comedy about the adventures of a legendary hotel concierge.");
        budapestHotel.setDuration(100);
        budapestHotel.setImageName("budapest_hotel.jpg");
        movieRepository.save(budapestHotel);

        Movie shawshank = new Movie();
        shawshank.setName("The Shawshank Redemption");
        shawshank.setDescription("A powerful story of hope and friendship in Shawshank State Penitentiary.");
        shawshank.setDuration(142);
        shawshank.setImageName("shawshank.jpg");
        movieRepository.save(shawshank);

        // Create seats for each movie
        for (Movie movie : movieRepository.findAll()) {
            for (char row = 'A'; row <= 'G'; row++) {
                for (int number = 1; number <= 14; number++) {
                    Seat seat = new Seat();
                    seat.setMovie(movie);
                    seat.setSeatRow(String.valueOf(row));
                    seat.setNumber(number);
                    seat.setOccupied(false);
                    seatRepository.save(seat);
                }
            }
        }
    }

    private void seedTestUser() {
        // Create test user if not exists
        if (userRepository.findByEmail("test@mail.com") == null) {
            User testUser = new User();
            testUser.setEmail("test@mail.com");
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setPassword("123Abc!");
            testUser.setRole("CUSTOMER");
            userRepository.save(testUser);
        }
    }
}