import React from 'react';

const MovieCard = ({ movie, onSelect }) => {
  return (
    <div
      className="bg-white rounded-lg shadow-lg overflow-hidden cursor-pointer transform transition-transform hover:scale-105"
      onClick={() => onSelect(movie)}
    >
      <img
        src={movie.imageName ? `/images/${movie.imageName}` : "/api/placeholder/300/400"}
        alt={movie.title}
        className="w-full h-64 object-cover"
      />
      <div className="p-4">
        <h3 className="text-xl font-bold text-gray-800 mb-2">{movie.title}</h3>
        <div className="text-sm text-gray-600">
          <p>Duration: {movie.duration} min</p>
        </div>
      </div>
    </div>
  );
};

export default MovieCard;