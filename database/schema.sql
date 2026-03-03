-- ============================================
-- Movie Ticket Booking System - Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS movie_booking_system;
USE movie_booking_system;

-- Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100),
    role ENUM('user','admin') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Movies Table
CREATE TABLE movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    genre VARCHAR(80),
    duration INT,
    language VARCHAR(30),
    rating DOUBLE DEFAULT 0,
    description TEXT,
    poster_color VARCHAR(20) DEFAULT '#E50914',
    status ENUM('Now Showing','Coming Soon') DEFAULT 'Now Showing',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Showtimes Table
CREATE TABLE showtimes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    theatre VARCHAR(50),
    show_date DATE,
    show_time VARCHAR(10),
    price DECIMAL(10,2),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Bookings Table
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    showtime_id INT,
    movie_title VARCHAR(150),
    theatre VARCHAR(50),
    show_date VARCHAR(20),
    show_time VARCHAR(10),
    seats VARCHAR(255),
    total_amount DECIMAL(10,2),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Confirmed','Cancelled') DEFAULT 'Confirmed',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id)
);

-- ========= DEFAULT DATA =========

-- Admin Account (username: admin, password: admin123)
INSERT INTO users (username, password, email, full_name, role)
VALUES ('admin', 'admin123', 'admin@cinemahub.com', 'Administrator', 'admin');

-- Sample User
INSERT INTO users (username, password, email, full_name, role)
VALUES ('john', 'john123', 'john@email.com', 'John Doe', 'user');

-- Sample Movies
INSERT INTO movies (title, genre, duration, language, rating, description, poster_color) VALUES
('Avengers: Endgame',    'Action / Sci-Fi',      181, 'English', 8.4,
 'The epic conclusion to the Infinity Saga.',                          '#E50914'),
('The Dark Knight',      'Action / Crime',        152, 'English', 9.0,
 'Batman faces his greatest nemesis, the Joker.',                      '#1A1A2E'),
('Inception',            'Sci-Fi / Thriller',     148, 'English', 8.8,
 'A thief steals secrets through dream-sharing technology.',           '#0F3460'),
('Interstellar',         'Sci-Fi / Drama',        169, 'English', 8.6,
 'Explorers travel through a wormhole in space.',                      '#16213E'),
('Spider-Man: No Way Home','Action / Adventure',  148, 'English', 8.3,
 'Peter Parker seeks help from Doctor Strange.',                       '#B20600'),
('Dune: Part Two',       'Sci-Fi / Adventure',    166, 'English', 8.5,
 'Paul Atreides unites with the Fremen.',                              '#C6A969'),
('Oppenheimer',          'Drama / History',       180, 'English', 8.9,
 'The story of J. Robert Oppenheimer.',                                '#4A0E0E'),
('Barbie',               'Comedy / Fantasy',      114, 'English', 7.0,
 'Barbie embarks on a journey of self-discovery.',                     '#FF69B4');

-- Showtimes for each movie
INSERT INTO showtimes (movie_id, theatre, show_date, show_time, price) VALUES
(1,'Screen 1 - IMAX',    CURDATE(),'10:00 AM', 300.00),
(1,'Screen 1 - IMAX',    CURDATE(),'02:00 PM', 350.00),
(1,'Screen 1 - IMAX',    CURDATE(),'06:00 PM', 400.00),
(2,'Screen 2 - Dolby',   CURDATE(),'11:00 AM', 280.00),
(2,'Screen 2 - Dolby',   CURDATE(),'03:00 PM', 320.00),
(2,'Screen 2 - Dolby',   CURDATE(),'07:00 PM', 380.00),
(3,'Screen 3 - Standard',CURDATE(),'10:30 AM', 250.00),
(3,'Screen 3 - Standard',CURDATE(),'02:30 PM', 300.00),
(3,'Screen 3 - Standard',CURDATE(),'06:30 PM', 350.00),
(4,'Screen 1 - IMAX',    CURDATE(),'09:00 PM', 420.00),
(5,'Screen 2 - Dolby',   CURDATE(),'12:00 PM', 300.00),
(5,'Screen 2 - Dolby',   CURDATE(),'04:00 PM', 340.00),
(6,'Screen 3 - Standard',CURDATE(),'01:00 PM', 280.00),
(6,'Screen 3 - Standard',CURDATE(),'05:00 PM', 320.00),
(7,'Screen 1 - IMAX',    CURDATE(),'11:30 AM', 350.00),
(7,'Screen 1 - IMAX',    CURDATE(),'03:30 PM', 400.00),
(8,'Screen 3 - Standard',CURDATE(),'12:30 PM', 250.00),
(8,'Screen 3 - Standard',CURDATE(),'04:30 PM', 280.00);