package com.example.MovieTheater.controller;

import com.example.MovieTheater.model.Seat;
import com.example.MovieTheater.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PostMapping
    public Seat createSeat(@RequestBody Seat seat) {
        return seatService.createSeat(seat);
    }


    @GetMapping("/{id}")
    public Seat getSeat(@PathVariable Long id) {
        return seatService.getSeat(id);
    }

}
