import React, { useState } from 'react';
import { bookingService } from '../../services/bookingService';

const BookingConfirmation = ({ selectedSeats, movieId, onSuccess, onCancel }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleConfirm = async () => {
    setLoading(true);
    setError(null);

    try {
      const bookingPromises = selectedSeats.map(seat => {
        const [row, number] = seat.match(/([A-G])(\d+)/).slice(1);
        return bookingService.createBooking({
          movieId: movieId,
          seatRow: row,
          number: parseInt(number)
        });
      });

      await Promise.all(bookingPromises);
      onSuccess();
    } catch (err) {
      setError('Failed to complete booking. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-6 rounded-lg max-w-md w-full">
        <h3 className="text-xl font-bold mb-4">Confirm Booking</h3>

        <div className="mb-4">
          <p className="mb-2">Selected Seats:</p>
          <div className="flex flex-wrap gap-2">
            {selectedSeats.map(seat => (
              <span key={seat} className="bg-blue-100 px-2 py-1 rounded">
                {seat}
              </span>
            ))}
          </div>
          <p className="mt-2">Total Price: {selectedSeats.length * 100} kr</p>
        </div>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        <div className="flex justify-end gap-4">
          <button
            onClick={onCancel}
            className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
            disabled={loading}
          >
            Cancel
          </button>
          <button
            onClick={handleConfirm}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Confirm Booking'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default BookingConfirmation;