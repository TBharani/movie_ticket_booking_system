import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        // Initialize theme
        ThemeManager.initLookAndFeel();

        // Launch application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Splash / Loading
            JFrame splash = new JFrame();
            splash.setUndecorated(true);
            splash.setSize(420, 260);
            splash.setLocationRelativeTo(null);

            GradientPanel sp = new GradientPanel(
                new Color(10, 10, 30), new Color(30, 10, 40)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                    // Border
                    g2.setColor(ThemeManager.PRIMARY);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                    // Icon
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
                    g2.setColor(ThemeManager.PRIMARY);
                    g2.drawString("M", 185, 75);

                    // Title
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 34));
                    g2.setColor(Color.WHITE);
                    String t = "CinemaHub";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2, 130);

                    // Subtitle
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    g2.setColor(ThemeManager.TEXT_MUTED);
                    String s = "Movie Ticket Booking System";
                    fm = g2.getFontMetrics();
                    g2.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, 155);

                    // Loading text
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    g2.setColor(ThemeManager.NEON_BLUE);
                    String l = "Loading...";
                    fm = g2.getFontMetrics();
                    g2.drawString(l, (getWidth() - fm.stringWidth(l)) / 2, 210);

                    // Loading bar background
                    g2.setColor(ThemeManager.SURFACE);
                    g2.fillRoundRect(60, 220, 300, 6, 3, 3);

                    // Loading bar fill
                    g2.setColor(ThemeManager.PRIMARY);
                    g2.fillRoundRect(60, 220, 200, 6, 3, 3);

                    g2.dispose();
                }
            };

            splash.setContentPane(sp);
            splash.setVisible(true);

            // Simulate loading then show login
            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    splash.dispose();
                    try {
                        // Test database connection
                        java.sql.Connection conn = DBConnection.getConnection();
                        conn.close();
                        System.out.println("Database connected successfully!");
                        new LoginFrame().setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                            "Cannot connect to database!\n\n" +
                            "Please ensure:\n" +
                            "1. MySQL/XAMPP is running\n" +
                            "2. Database 'movie_booking_system' exists\n" +
                            "3. Run schema.sql to create tables\n\n" +
                            "Error: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}