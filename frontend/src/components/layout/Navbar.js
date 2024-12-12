import React from 'react';

const Navbar = ({
  isLoggedIn = false,
  user = null,
  onLogout = () => {},
  currentPage = 'home',
  onNavigate = () => {}
}) => {
  return (
    <nav className="bg-blue-900 shadow-lg">
      <div className="container mx-auto px-4 py-3">
        <div className="flex justify-between items-center">
          <div className="flex space-x-6">
            <button
              onClick={() => onNavigate('home')}
              className={`text-white hover:text-blue-200 transition-colors duration-200
                ${currentPage === 'home' ? 'border-b-2 border-white' : ''}`}
            >
              Home
            </button>
            <button
              onClick={() => onNavigate('movies')}
              className={`text-white hover:text-blue-200 transition-colors duration-200
                ${currentPage === 'movies' ? 'border-b-2 border-white' : ''}`}
            >
              Movies
            </button>
            {isLoggedIn && (
              <button
                onClick={() => onNavigate('bookings')}
                className={`text-white hover:text-blue-200 transition-colors duration-200
                  ${currentPage === 'bookings' ? 'border-b-2 border-white' : ''}`}
              >
                Bookings
              </button>
            )}
          </div>

          <div>
            {isLoggedIn ? (
              <div className="flex items-center space-x-4">
                <span className="text-white">
                  {user?.firstName} {user?.lastName}
                </span>
                <button
                  onClick={onLogout}
                  className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg
                    transition-colors duration-200 shadow-md"
                >
                  Logout
                </button>
              </div>
            ) : (
              <div className="space-x-3">
                <button
                  onClick={() => onNavigate('login')}
                  className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg
                    transition-colors duration-200 shadow-md"
                >
                  Login
                </button>
                <button
                  onClick={() => onNavigate('register')}
                  className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg
                    transition-colors duration-200 shadow-md"
                >
                  Register
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;