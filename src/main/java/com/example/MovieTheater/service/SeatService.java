package com.example.MovieTheater.service;



import com.example.MovieTheater.model.Seat;
import com.example.MovieTheater.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat getSeat(Long id) {
        return seatRepository.findById(id).orElse(null);
    }

    // Other methods for booking, changing seat status, etc.
}
