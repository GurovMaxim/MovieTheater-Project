import React, { useState } from 'react';
import Navbar from './components/layout/Navbar';
import LoginForm from './components/auth/LoginForm';
import RegisterForm from './components/auth/RegisterForm';
import MovieGrid from './components/movie/MovieGrid';
import MovieDetails from './components/movie/MovieDetails';
import BookingConfirmation from './components/booking/BookingConfirmation';
import BookingsPage from './components/booking/BookingsPage';

const App = () => {
  const [currentPage, setCurrentPage] = useState('home');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [showBookingConfirmation, setShowBookingConfirmation] = useState(false);
  const [selectedSeats, setSelectedSeats] = useState([]);

  const handleNavigate = (page) => {
    setCurrentPage(page);
    if (page === 'movies') {
      setSelectedMovie(null);
    }
  };

  const handleMovieSelect = (movie) => {
    setSelectedMovie(movie);
    setCurrentPage('movie-details');
  };

  const handleBackToMovies = () => {
    setSelectedMovie(null);
    setCurrentPage('movies');
  };

  const handleLogin = async (userData) => {
    try {
      console.log('Login successful, token:', userData.token);
      localStorage.setItem('token', userData.token);
      setUser(userData);
      setIsLoggedIn(true);
      setCurrentPage('home');
    } catch (error) {
      console.error('Login error:', error);
    }
  };

  const handleRegister = async (userData) => {
    try {
      setUser(userData);
      setIsLoggedIn(true);
      setCurrentPage('home');
    } catch (error) {
      console.error('Registration error:', error);
    }
  };

  const handleLogout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Always clear state and localStorage
      localStorage.removeItem('token');
      setIsLoggedIn(false);
      setUser(null);
      setCurrentPage('home');
    }
  };

  const handleBookingConfirm = (seats) => {
    setSelectedSeats(seats);
    setShowBookingConfirmation(true);
  };

  const handleBookingSuccess = () => {
    setShowBookingConfirmation(false);
    setSelectedSeats([]);
    handleBackToMovies();
  };

  const handleBookingCancel = () => {
    setShowBookingConfirmation(false);
    setSelectedSeats([]);
  };

  const renderContent = () => {
    switch (currentPage) {
      case 'login':
        return <LoginForm onLogin={handleLogin} />;
      case 'register':
        return <RegisterForm onRegister={handleRegister} />;
      case 'movies':
        return <MovieGrid onMovieSelect={handleMovieSelect} />;
      case 'bookings':
        return <BookingsPage />;
      case 'movie-details':
        return selectedMovie ? (
          <MovieDetails
            movie={selectedMovie}
            onBack={handleBackToMovies}
            onBookingConfirm={handleBookingConfirm}
          />
        ) : (
          handleNavigate('movies')
        );
      default:
        return (
          <h1 className="text-3xl font-bold text-gray-800">
            Welcome to Movie Theater
          </h1>
        );
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar
        isLoggedIn={isLoggedIn}
        user={user}
        onLogout={handleLogout}
        currentPage={currentPage}
        onNavigate={handleNavigate}
      />
      <main className="container mx-auto px-4 py-8">
        {renderContent()}
      </main>
      {showBookingConfirmation && (
        <BookingConfirmation
          selectedSeats={selectedSeats}
          movieId={selectedMovie?.id}
          onSuccess={handleBookingSuccess}
          onCancel={handleBookingCancel}
        />
      )}
    </div>
  );
};

export default App;