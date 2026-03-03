import java.sql.*;
import java.util.*;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/movie_booking_system";
    private static final String USER = "root";
    private static final String PASS = "bharani";   // change if your MySQL has a password

    // Load driver once when class is first used
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-java JAR is in the classpath.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /* ── AUTHENTICATION ── */
    public static User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                    rs.getString("password"), rs.getString("email"),
                    rs.getString("full_name"), rs.getString("role"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean registerUser(String user, String pass,
                                       String email, String name) {
        String sql = "INSERT INTO users(username,password,email,full_name) VALUES(?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.setString(4, name);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /* ── MOVIES ── */
    public static List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY id DESC";
        try (Connection c = getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Movie(
                    rs.getInt("id"), rs.getString("title"),
                    rs.getString("genre"), rs.getInt("duration"),
                    rs.getString("language"), rs.getDouble("rating"),
                    rs.getString("description"), rs.getString("poster_color"),
                    rs.getString("status")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean addMovie(String title, String genre, int dur,
            String lang, double rating, String desc, String color, String status) {
        String sql = "INSERT INTO movies(title,genre,duration,language,rating," +
                     "description,poster_color,status) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title); ps.setString(2, genre);
            ps.setInt(3, dur);      ps.setString(4, lang);
            ps.setDouble(5, rating);ps.setString(6, desc);
            ps.setString(7, color); ps.setString(8, status);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean updateMovie(int id, String title, String genre,
            int dur, String lang, double rating, String desc, String color, String status) {
        String sql = "UPDATE movies SET title=?,genre=?,duration=?,language=?," +
                     "rating=?,description=?,poster_color=?,status=? WHERE id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title); ps.setString(2, genre);
            ps.setInt(3, dur);      ps.setString(4, lang);
            ps.setDouble(5, rating);ps.setString(6, desc);
            ps.setString(7, color); ps.setString(8, status);
            ps.setInt(9, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean deleteMovie(int id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM movies WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /* ── SHOWTIMES ── */
    public static List<String[]> getShowtimes(int movieId) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM showtimes WHERE movie_id=? ORDER BY show_time";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("theatre"),
                    rs.getString("show_date"),
                    rs.getString("show_time"),
                    String.valueOf(rs.getDouble("price"))
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean addShowtime(int movieId, String theatre,
                                      String date, String time, double price) {
        String sql = "INSERT INTO showtimes(movie_id,theatre,show_date,show_time,price) " +
                     "VALUES(?,?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, movieId); ps.setString(2, theatre);
            ps.setString(3, date); ps.setString(4, time);
            ps.setDouble(5, price);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /* ── BOOKED SEATS ── */
    public static Set<String> getBookedSeats(int showtimeId) {
        Set<String> seats = new HashSet<>();
        String sql = "SELECT seats FROM bookings WHERE showtime_id=? AND status='Confirmed'";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String s = rs.getString("seats");
                if (s != null && !s.isEmpty()) {
                    for (String seat : s.split(","))
                        seats.add(seat.trim());
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return seats;
    }

    /* ── BOOKINGS ── */
    public static boolean createBooking(int userId, int showtimeId,
            String movieTitle, String theatre, String showDate,
            String showTime, String seats, double total) {
        String sql = "INSERT INTO bookings(user_id,showtime_id,movie_title," +
                     "theatre,show_date,show_time,seats,total_amount) " +
                     "VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);       ps.setInt(2, showtimeId);
            ps.setString(3, movieTitle);ps.setString(4, theatre);
            ps.setString(5, showDate);  ps.setString(6, showTime);
            ps.setString(7, seats);     ps.setDouble(8, total);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static List<String[]> getUserBookings(int userId) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id=? ORDER BY booking_date DESC";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("movie_title"),
                    rs.getString("theatre"),
                    rs.getString("show_date"),
                    rs.getString("show_time"),
                    rs.getString("seats"),
                    String.valueOf(rs.getDouble("total_amount")),
                    rs.getString("status"),
                    rs.getString("booking_date")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static List<String[]> getAllBookings() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT b.*, u.username, u.full_name FROM bookings b " +
                     "JOIN users u ON b.user_id=u.id ORDER BY b.booking_date DESC";
        try (Connection c = getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("movie_title"),
                    rs.getString("theatre"),
                    rs.getString("show_date"),
                    rs.getString("show_time"),
                    rs.getString("seats"),
                    String.valueOf(rs.getDouble("total_amount")),
                    rs.getString("status"),
                    rs.getString("booking_date")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean cancelBooking(int bookingId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "UPDATE bookings SET status='Cancelled' WHERE id=?")) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}