const API_URL = 'http://localhost:8082/api';

export const movieService = {
  getAllMovies: async () => {
    try {
      const response = await fetch(`${API_URL}/movies`);
      if (!response.ok) {
        throw new Error('Failed to fetch movies');
      }
      return response.json();
    } catch (error) {
      console.error('Error fetching movies:', error);
      throw error;
    }
  },

  getMovieById: async (id) => {
    try {
      const movieResponse = await fetch(`${API_URL}/movies/${id}`);
      if (!movieResponse.ok) {
        throw new Error('Failed to fetch movie details');
      }
      const movie = await movieResponse.json();

      // Fetch seats for this movie
      const seatsResponse = await fetch(`${API_URL}/movies/${id}/seats`);
      if (!seatsResponse.ok) {
        throw new Error('Failed to fetch seats');
      }
      const seats = await seatsResponse.json();

      return { ...movie, seats };
    } catch (error) {
      console.error('Error fetching movie details:', error);
      throw error;
    }
  }
};