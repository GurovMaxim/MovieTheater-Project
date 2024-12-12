const API_URL = 'http://localhost:8082/api';

export const authService = {
  login: async (email, password) => {
    try {
      console.log('Attempting login with email:', email);

      const response = await fetch(`${API_URL}/users/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password })
      });

      console.log('Login response status:', response.status);

      if (!response.ok) {
        throw new Error('Invalid credentials');
      }

      const token = await response.text();
      console.log('Received token:', token);

      localStorage.setItem('token', token);
      console.log('Token stored:', localStorage.getItem('token'));

      return {
        token,
        email
      };
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  register: async (userData) => {
    try {
      const response = await fetch(`${API_URL}/users/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
      });

      const responseText = await response.text();
      console.log('Registration response:', responseText); // Debug log

      if (!response.ok) {
        throw new Error(`Registration failed: ${responseText}`);
      }

      return responseText;
    } catch (error) {
      console.error('Registration error details:', error);
      throw error;
    }
  },

  logout: async () => {
    try {
      const token = localStorage.getItem('token');
      console.log('Token before logout:', token); // Debug log

      if (!token) return;

      const response = await fetch(`${API_URL}/users/logout`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      localStorage.removeItem('token');
      console.log('Token removed from localStorage'); // Debug log

      return response.text();
    } catch (error) {
      console.error('Logout error details:', error);
      localStorage.removeItem('token');
      throw error;
    }
  }
};