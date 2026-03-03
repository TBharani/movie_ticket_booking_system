## Movie Ticket Booking System

A Java-based movie ticket booking application with user authentication, seat selection, and booking management.

## Folder Structure

```
movie/
├── database/
│   └── schema.sql
├── lib/
│   └── mysql-connector-j-9.6.0.jar
├── src/
│   ├── AddMovieDialog.java
│   ├── AdminDashboard.java
│   ├── BookingConfirmationFrame.java
│   ├── BookingHistoryFrame.java
│   ├── DBConnection.java
│   ├── GradientPanel.java
│   ├── LoginFrame.java
│   ├── Main.java
│   ├── Movie.java
│   ├── MovieCard.java
│   ├── RegisterFrame.java
│   ├── RoundedButton.java
│   ├── RoundedPasswordField.java
│   ├── RoundedTextField.java
│   ├── SeatButton.java
│   ├── SeatSelectionFrame.java
│   ├── ThemeManager.java
│   ├── TicketPanel.java
│   ├── User.java
│   ├── UserDashboard.java
│   └── ViewBookingsFrame.java
└── README.md
```

## How to Run

### Prerequisites
- Java JDK 8 or higher
- MySQL database
- MySQL Connector JAR (included in `lib/` folder)

### Setup Instructions

1. **Setup Database:**
   - Import the database schema from `database/schema.sql` into your MySQL database

2. **Configure Database Connection:**
   - Update the database credentials in `DBConnection.java` if needed

3. **Compile the Project:**
   ```bash
   cd src
   javac -cp ".;../lib/mysql-connector-j-9.6.0.jar" *.java
   ```

4. **Run the Application:**
   ```bash
   java -cp ".;../lib/mysql-connector-j-9.6.0.jar" Main
   ```

## Dependency Management

- MySQL Connector: `lib/mysql-connector-j-9.6.0.jar`

The project uses MySQL JDBC driver for database connectivity. All dependencies are included in the `lib` folder.
