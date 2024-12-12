import React, { useState, useEffect } from 'react';
import { movieService } from '../../services/movieService';
import { bookingService } from '../../services/bookingService';

const MovieDetails = ({ movie, onBack }) => {
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [seats, setSeats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];
  const seatsPerRow = 14;

  useEffect(() => {
    fetchMovieDetails();
  }, [movie.id]);

  const fetchMovieDetails = async () => {
    try {
      const movieData = await movieService.getMovieById(movie.id);
      console.log('Full movie data:', movieData);
      console.log('Sample seat:', movieData.seats[0]); // Add this line to see one seat's structure
      console.log('Seats array:', movieData.seats);
      setSeats(movieData.seats || []);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching movie details:', err);
      setError('Failed to load movie details');
      setLoading(false);
    }
  };

  const handleSeatClick = (seatId) => {
    setSelectedSeats(prev => {
      if (prev.includes(seatId)) {
        return prev.filter(id => id !== seatId);
      }
      return [...prev, seatId];
    });
  };

  const handleBooking = async () => {
    if (!selectedSeats.length) return;

    try {
      const token = localStorage.getItem('token');
      console.log('Current token:', token); // Add this log

      for (const seat of selectedSeats) {
        const [row, number] = seat.match(/([A-G])(\d+)/).slice(1);
        console.log('Booking seat:', { // Add this log
          movieId: movie.id,
          seatRow: row,
          number: parseInt(number)
        });

        await bookingService.createBooking({
          movieId: movie.id,
          seatRow: row,
          number: parseInt(number)
        });
      }

      alert('Booking successful!');
      setSelectedSeats([]);
      fetchMovieDetails();
    } catch (err) {
      console.error('Booking error details:', err);
      alert('Failed to book seats. Please try again.');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-xl text-gray-600">Loading movie details...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto">
      <button
        onClick={onBack}
        className="mb-6 text-blue-600 hover:text-blue-800 flex items-center"
      >
        ← Back to Movies
      </button>

      <div className="bg-white rounded-lg shadow-lg p-6 mb-8">
        <div className="flex flex-col md:flex-row gap-8">
          <img
            src={movie.imageName ? `/images/${movie.imageName}` : "/api/placeholder/400/600"}
            alt={movie.name}
            className="w-full md:w-1/3 rounded-lg object-cover"
          />
          <div>
            <h1 className="text-3xl font-bold mb-4">{movie.name}</h1>
            <div className="space-y-4 text-gray-600">
              <p>Duration: {movie.duration} minutes</p>
              <p className="text-lg mt-2">{movie.description}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-lg p-6">
        <h2 className="text-2xl font-bold mb-6">Select Seats</h2>

        <div className="mb-8 w-full h-4 bg-gray-300 rounded-lg">
          <div className="text-center text-sm text-gray-600">Screen</div>
        </div>

        <div className="space-y-4">
          {rows.map(row => (
            <div key={row} className="flex justify-center space-x-2">
              {/* Row label */}
              <div className="w-8 flex items-center justify-center font-bold">
                {row}
              </div>

              {/* Seats */}
              {[...Array(seatsPerRow)].map((_, index) => {
                const seatNumber = (index + 1).toString().padStart(2, '0');
                const seatId = `${row}${seatNumber}`;
                // Find matching seat from our seats array
                const seat = seats.find(s => s.seatRow === row && s.number === (index + 1));

                console.log(`Rendering seat ${seatId}:`, {
                  isOccupied: seat?.occupied,
                  isSelected: selectedSeats.includes(seatId),
                  color: seat?.occupied ? 'red' : selectedSeats.includes(seatId) ? 'yellow' : 'green'
                });

                return (
                  <button
                    key={seatId}
                    disabled={seat?.occupied}
                    onClick={() => !seat?.occupied && handleSeatClick(seatId)}
                    style={{
                      backgroundColor: seat?.occupied ? 'red' :
                                     selectedSeats.includes(seatId) ? 'yellow' :
                                     'green'
                    }}
                    className="w-8 h-8 rounded"
                  >
                    <span className="text-xs text-white">{seatNumber}</span>
                  </button>
                );
              })}
            </div>
          ))}
        </div>

        <div className="mt-8 flex justify-between items-center">
          <div className="flex space-x-4">
            <div className="flex items-center">
              <div className="w-4 h-4 bg-green-500 rounded mr-2" />
              <span>Available</span>
            </div>
            <div className="flex items-center">
              <div className="w-4 h-4 bg-yellow-400 rounded mr-2" />
              <span>Selected</span>
            </div>
            <div className="flex items-center">
              <div className="w-4 h-4 bg-red-500 rounded mr-2" />
              <span>Occupied</span>
            </div>
          </div>
          <div>
            <div className="text-xl font-bold mb-2">
              Total: {selectedSeats.length * 100} kr
            </div>
            <button
              onClick={handleBooking}
              disabled={selectedSeats.length === 0}
              className={`
                px-6 py-2 rounded
                ${selectedSeats.length > 0
                  ? 'bg-blue-600 hover:bg-blue-700'
                  : 'bg-gray-400 cursor-not-allowed'}
                text-white font-medium
                transition-colors duration-200
              `}
            >
              Book Seats
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MovieDetails;