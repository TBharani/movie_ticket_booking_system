import javax.swing.*;
import java.awt.*;

public class BookingConfirmationFrame extends JFrame {

    public BookingConfirmationFrame(User user, String movieTitle,
            String theatre, String showDate, String showTime,
            String seats, double total) {

        setTitle("CinemaHub - Booking Confirmed!");
        setSize(700, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        GradientPanel bg = new GradientPanel(
            ThemeManager.BG_DARK, new Color(15, 15, 40));
        bg.setLayout(new BoxLayout(bg, BoxLayout.Y_AXIS));
        bg.setBorder(ThemeManager.padding(30, 40, 30, 40));
        setContentPane(bg);

        // Success icon & message
        JLabel checkIcon = ThemeManager.centeredLabel("✅",
            new Font("Segoe UI Emoji", Font.PLAIN, 48), ThemeManager.SUCCESS);
        checkIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(checkIcon);
        bg.add(Box.createVerticalStrut(8));

        JLabel title = ThemeManager.centeredLabel("Booking Confirmed!",
            ThemeManager.HEADING, ThemeManager.SUCCESS);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(title);
        bg.add(Box.createVerticalStrut(5));

        JLabel sub = ThemeManager.centeredLabel(
            "Your tickets have been booked successfully.",
            ThemeManager.BODY, ThemeManager.TEXT_SECONDARY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(sub);
        bg.add(Box.createVerticalStrut(25));

        // Ticket
        String bookingId = String.valueOf(System.currentTimeMillis() % 100000);
        TicketPanel ticket = new TicketPanel(movieTitle, theatre,
            showDate, showTime, seats, total, bookingId);
        ticket.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(ticket);
        bg.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnP.setOpaque(false);
        btnP.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton browseBtn = new RoundedButton("🎬 Browse Movies",
            ThemeManager.PRIMARY, ThemeManager.PRIMARY_HOVER);
        browseBtn.setPreferredSize(new Dimension(180, 42));
        browseBtn.addActionListener(e -> {
            dispose();
        });
        btnP.add(browseBtn);

        RoundedButton historyBtn = new RoundedButton("📋 My Bookings",
            ThemeManager.PURPLE, ThemeManager.PURPLE.brighter());
        historyBtn.setPreferredSize(new Dimension(180, 42));
        historyBtn.addActionListener(e -> {
            dispose();
            new BookingHistoryFrame(user).setVisible(true);
        });
        btnP.add(historyBtn);

        bg.add(btnP);
    }
}