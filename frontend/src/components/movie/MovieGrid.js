import React, { useState, useEffect } from 'react';
import { movieService } from '../../services/movieService';
import MovieCard from './MovieCard';

const MovieGrid = ({ onMovieSelect }) => {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const data = await movieService.getAllMovies();
        console.log('Fetched movies:', data); // debug
        setMovies(data);
        setLoading(false);
      } catch (err) {
        console.error('Error:', err); // Added error logging
        setError('Failed to load movies');
        setLoading(false);
      }
    };

    fetchMovies();
  }, []);

  const handleMovieSelect = async (movie) => {
    try {
      const movieDetails = await movieService.getMovieById(movie.id);
      onMovieSelect(movieDetails);
    } catch (err) {
      console.error('Error fetching movie details:', err);
      // Optionally show error to user
      // setError('Failed to load movie details');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-xl text-gray-600">Loading movies...</div>
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
    <div>
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Available Movies</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {movies.map(movie => (
          <MovieCard
            key={movie.id}
            movie={movie}
            onSelect={() => handleMovieSelect(movie)}
          />
        ))}
      </div>
    </div>
  );
};

export default MovieGrid;