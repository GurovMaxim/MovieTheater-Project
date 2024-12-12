Movie Theater Booking System
Project Overview
A web application for booking movie tickets. Users can view available movies, select seats, and make bookings after logging in. Built with Spring Boot backend and React frontend.
Features

User authentication (login/register)
Movie listing with details
Interactive seat selection
Booking management
Real-time seat availability status

Technical Stack
Backend (Java Spring Boot)

Java 17
Spring Boot
Spring Security with JWT
MySQL Database
Swagger UI for API documentation

Frontend (React)

React.js
Tailwind CSS for styling
Local storage for authentication token

Setup Instructions
Prerequisites

Java 17
MySQL
Node.js and npm
Maven

Database Setup

Create MySQL database:


Configure database connection in application.properties:

propertiesCopyspring.datasource.url=jdbc:mysql://localhost:3307/movie_theater
spring.datasource.username=root
spring.datasource.password=root

DataSeeder.java creates 4 movies and a user by default.

Backend Setup

Clone repository
Navigate to project root directory
Build project:

bashCopymvn clean install

Run application:

bashCopymvn spring-boot:run
Backend will run on http://localhost:8082
Frontend Setup

Navigate to frontend directory:

bashCopycd frontend

Install dependencies:

bashCopynpm install

Start frontend application:

bashCopynpm start
Frontend will run on http://localhost:3000
API Documentation
API documentation is available through Swagger UI at:
http://localhost:8082/swagger-ui.html
