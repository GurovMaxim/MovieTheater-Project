import React, { useState, useEffect } from 'react';
import { bookingService } from '../../services/bookingService';

const BookingsPage = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const data = await bookingService.getUserBookings();
      console.log('Fetched bookings:', data);
      setBookings(data);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching bookings:', err);
      setError('Failed to load bookings');
      setLoading(false);
    }
  };

  if (loading) return <div>Loading bookings...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="container mx-auto p-4">
      <h2 className="text-2xl font-bold mb-4">Your Bookings</h2>
      {bookings.length === 0 ? (
        <p>No bookings found</p>
      ) : (
        <div className="grid gap-4">
          {bookings.map((booking) => (
            <div key={booking.id} className="bg-white p-4 rounded shadow">
              <h3 className="font-bold">{booking.seat?.movie?.name}</h3>
              <p>Seat: Row {booking.seat?.seatRow}, Number {booking.seat?.number}</p>
              <p>Status: {booking.confirmed ? 'Confirmed' : 'Pending'}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default BookingsPage;