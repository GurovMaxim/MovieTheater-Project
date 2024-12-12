const API_URL = 'http://localhost:8082/api';

export const bookingService = {
  createBooking: async (bookingData) => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Authentication required');
      }

      const response = await fetch(`${API_URL}/bookings`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token
        },
        body: JSON.stringify(bookingData)
      });

      if (!response.ok) {
        throw new Error('Booking failed');
      }

      return response.json();
    } catch (error) {
      console.error('Booking error:', error);
      throw error;
    }
  },

  getUserBookings: async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('Authentication required');
      }

      const response = await fetch(`${API_URL}/bookings/user`, {
        headers: {
          'Authorization': token
        }
      });

      if (!response.ok) {
        throw new Error('Failed to fetch bookings');
      }

      return response.json();
    } catch (error) {
      console.error('Error fetching bookings:', error);
      throw error;
    }
  }
};